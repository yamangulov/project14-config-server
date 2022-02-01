package org.satel.eip.project14.config.server.domain.config.common;

import lombok.extern.slf4j.Slf4j;
import org.satel.eip.project14.config.server.domain.config.internal.entity.Clients;
import org.satel.eip.project14.config.server.domain.config.external.ExternalConfigService;
import org.satel.eip.project14.config.server.domain.config.external.entity.ExternalConfigEntity;
import org.satel.eip.project14.config.server.domain.config.external.exception.ExternalConfigServiceGenericException;
import org.satel.eip.project14.config.server.domain.config.internal.InternalConfigService;
import org.satel.eip.project14.config.server.domain.config.internal.entity.InternalConfigEntity;
import org.satel.eip.project14.config.server.domain.config.internal.exception.InternalConfigServiceGenericException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RefreshService {

    private final Clients clients;
    private final ExternalConfigService externalConfigService;
    private final InternalConfigService internalConfigService;

    @Autowired
    public RefreshService(Clients clients, ExternalConfigService externalConfigService, InternalConfigService internalConfigService) {
        this.clients = clients;
        this.externalConfigService = externalConfigService;
        this.internalConfigService = internalConfigService;
    }

    public void refreshOnCheckConfigVersion() {
        for (Clients.Client client : clients.getClients()) {
            try {
                InternalConfigEntity internalConfigEntity = internalConfigService.getInternalConfigByClient(client);
                ExternalConfigEntity externalConfigEntity = externalConfigService.getExternalConfigByClient(client);

                if (internalConfigEntity.isNotValid()) {
                    log.info("Значение версии клиента {} на сервере не задано, будет выполнено принудительное " +
                            "обновление конфигурации клиента", client.getName());
                    //TODO подключить кастомную метрику
                    externalConfigService.forceRefresh(client);
                } else if (externalConfigEntity.isNotValid()) {
                    log.info("Значение версии клиента {} на клиенте не задано, будет выполнено принудительное " +
                            "обновление конфигурации клиента", client.getName());
                    //TODO подключить кастомную метрику
                    externalConfigService.forceRefresh(client);
                } else if (!internalConfigEntity.getConfigVersion().equals(externalConfigEntity.getExternalConfigProperty().getValue())) {
                    log.info("Значение версии клиента {} на клиенте и на сервере не совпадают, будет выполнено " +
                            "принудительное обновление конфигурации клиента", client.getName());
                    //TODO подключить кастомную метрику
                    externalConfigService.forceRefresh(client);
                } else {
                    log.info("Значение версии клиента {} на клиенте и на сервере совпадают, " +
                            "конфигурация клиента не нуждается в обновлении", client.getName());
                    //TODO подключить кастомную метрику
                }

            } catch (InternalConfigServiceGenericException e) {
                log.error(e.getMessage());
                //TODO подключить кастомную метрику
                //На самом деле последующие catch не будут просить схлопываться с предыдущим, когда будут добавлены
                //различные метрики в каждом блоке catch
            } catch (ExternalConfigServiceGenericException e) {
                log.error(e.getMessage());
                //TODO подключить кастомную метрику
            } catch (Throwable e) {
                log.error(e.getMessage());
                //TODO подключить кастомную метрику, здесь отлавливаются только неучтенные в сервисах остальные ошибки
            }
        }
    }


}
