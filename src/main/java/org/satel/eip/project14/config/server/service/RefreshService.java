package org.satel.eip.project14.config.server.service;

import lombok.extern.slf4j.Slf4j;
import org.satel.eip.project14.config.server.data.application.Clients;
import org.satel.eip.project14.config.server.domain.config.external.ExternalConfigService;
import org.satel.eip.project14.config.server.domain.config.external.entity.ExternalConfigEntity;
import org.satel.eip.project14.config.server.domain.config.internal.InternalConfigService;
import org.satel.eip.project14.config.server.domain.config.internal.entity.InternalConfigEntity;
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

                if (!internalConfigEntity.isValid() || !externalConfigEntity.isValid()) {
                    throw new Exception("blah blah blah");
                }

                if (!internalConfigEntity.getConfigVersion().equals(externalConfigEntity.getExternalConfigProperty().getValue())) {
                    externalConfigService.forceRefresh(client);
                }

            } catch (Throwable e) {
                //TODO ловить разные исключения или GenericException
                log.error(e.getMessage());
                //TODO подключить кастомную метрику
            }
        }
    }


}
