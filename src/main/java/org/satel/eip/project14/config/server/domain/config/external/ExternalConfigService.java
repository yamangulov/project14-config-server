package org.satel.eip.project14.config.server.domain.config.external;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.satel.eip.project14.config.server.data.application.Clients;
import org.satel.eip.project14.config.server.domain.config.external.entity.ExternalConfigEntity;
import org.satel.eip.project14.config.server.service.ClientPath;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class ExternalConfigService {

    private final ExternalConfigRepository externalConfigEntity;

    public ExternalConfigService(ExternalConfigRepository externalConfigEntity) {
        this.externalConfigEntity = externalConfigEntity;
    }

    @Value("${application.refreshDelayInMs}")
    private int delay;


    public ExternalConfigEntity getExternalConfigByClient(@NonNull Clients.Client client) {

        if (!client.isValid()) {
            throw new Exception("blah blah blah");
        }

        BasicAuthenticationInterceptor interceptor = new BasicAuthenticationInterceptor(client.getUser(), client.getPassword());
        ResponseEntity<ExternalConfigEntity> responseEntity = externalConfigEntity.getExternalConfigEntityByUrl(interceptor, client.getProtocol() + "://" + client.getUrl() + ClientPath.ACTUATOR_CONFIG_VERSION.getPath());


        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            throw new Exception("blah blah blah");
            //TODO подключить кастомную метрику
        }


        ExternalConfigEntity externalConfigEntity = responseEntity.getBody();

        if (externalConfigEntity == null || !externalConfigEntity.isValid()) {
            throw new Exception("blah blah blah");
            //TODO подключить кастомную метрику
        }

        //TODO подключить кастомную метрику
        return externalConfigEntity;
    }

    public void forceRefresh(Clients.Client client) {
        BasicAuthenticationInterceptor interceptor = new BasicAuthenticationInterceptor(client.getUser(), client.getPassword());
        String url = client.getProtocol() + "://" + client.getUrl() + ClientPath.ACTUATOR_REFRESH.getPath();

        ResponseEntity<String> responseEntity = externalConfigEntity.forceRefresh(interceptor, url);

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            throw new Exception("blah blah blah");
            //TODO подключить кастомную метрику
        }
    }

}