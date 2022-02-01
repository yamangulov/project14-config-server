package org.satel.eip.project14.config.server.domain.config.external;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.satel.eip.project14.config.server.domain.config.internal.entity.Clients;
import org.satel.eip.project14.config.server.domain.config.external.entity.ExternalConfigEntity;
import org.satel.eip.project14.config.server.domain.config.external.exception.ExternalConfigServiceGenericException;
import org.satel.eip.project14.config.server.domain.config.external.exception.GetExternalConfigByClientHttpStatusException;
import org.satel.eip.project14.config.server.domain.config.external.exception.GetExternalConfigByClientNotValidEntityException;
import org.satel.eip.project14.config.server.domain.config.common.entity.ClientPath;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ExternalConfigService {

    private final ExternalConfigRepository externalConfigRepository;

    public ExternalConfigService(ExternalConfigRepository externalConfigRepository) {
        this.externalConfigRepository = externalConfigRepository;
    }

    public ExternalConfigEntity getExternalConfigByClient(@NonNull Clients.Client client) throws ExternalConfigServiceGenericException {

        if (client.isNotValid()) {
            throw new GetExternalConfigByClientNotValidEntityException("Не все поля, необходимые для REST подключения к клиенту " + client.getName() + " заданы в конфигурационном файле сервера");
        }

        BasicAuthenticationInterceptor interceptor = new BasicAuthenticationInterceptor(client.getUser(), client.getPassword());
        ResponseEntity<ExternalConfigEntity> responseEntity = externalConfigRepository.getExternalConfigEntityByUrl(interceptor, client.getProtocol() + "://" + client.getUrl() + ClientPath.ACTUATOR_CONFIG_VERSION.getPath());


        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            throw new GetExternalConfigByClientHttpStatusException("Клиент " + client.getName() + " ответил со статусом " + responseEntity.getStatusCodeValue() + ", невозможно получить текущую версию клиента");
        }

        ExternalConfigEntity externalConfigEntity = responseEntity.getBody();

        if (externalConfigEntity == null || !externalConfigEntity.isNotValid()) {
            throw new GetExternalConfigByClientNotValidEntityException("Клиент " + client.getName() + " не прислал корректное тело ответного сообщения, невозможно получить текущую версию клиента");
        }

        return externalConfigEntity;
    }

    public void forceRefresh(Clients.Client client) throws ExternalConfigServiceGenericException {
        BasicAuthenticationInterceptor interceptor = new BasicAuthenticationInterceptor(client.getUser(), client.getPassword());
        String url = client.getProtocol() + "://" + client.getUrl() + ClientPath.ACTUATOR_REFRESH.getPath();

        ResponseEntity<String> responseEntity = externalConfigRepository.forceRefresh(interceptor, url);

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            throw new GetExternalConfigByClientHttpStatusException("Клиент " + client.getName() + " ответил со статусом " + responseEntity.getStatusCodeValue() + " при попытке форсированного обновления конфигурации, фактическое обновление конфигурации не гарантируется и будет повторено при следующем запуске цикла обновления на сервере");
        }
    }

}
