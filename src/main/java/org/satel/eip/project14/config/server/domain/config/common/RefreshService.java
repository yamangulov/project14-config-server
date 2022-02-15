package org.satel.eip.project14.config.server.domain.config.common;

import lombok.extern.slf4j.Slf4j;
import org.satel.eip.project14.config.server.domain.config.internal.entity.Clients;
import org.satel.eip.project14.config.server.domain.config.external.ExternalConfigService;
import org.satel.eip.project14.config.server.domain.config.external.entity.ExternalConfigEntity;
import org.satel.eip.project14.config.server.domain.config.external.exception.ExternalConfigServiceGenericException;
import org.satel.eip.project14.config.server.domain.config.internal.InternalConfigService;
import org.satel.eip.project14.config.server.domain.config.internal.entity.InternalConfigEntity;
import org.satel.eip.project14.config.server.domain.config.internal.exception.InternalConfigServiceGenericException;
import org.satel.eip.project14.config.server.metrics.accumulator.AccumulatorService;
import org.satel.eip.project14.config.server.metrics.accumulator.entity.AvailableMetrics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RefreshService {

    private final Clients clients;
    private final ExternalConfigService externalConfigService;
    private final InternalConfigService internalConfigService;
    private final AccumulatorService accumulatorService;

    @Autowired
    public RefreshService(Clients clients, ExternalConfigService externalConfigService, InternalConfigService internalConfigService, AccumulatorService accumulatorService) {
        this.clients = clients;
        this.externalConfigService = externalConfigService;
        this.internalConfigService = internalConfigService;
        this.accumulatorService = accumulatorService;
    }

    public void refreshOnCheckConfigVersion() {
        for (Clients.Client client : clients.getClients()) {
            try {
                InternalConfigEntity internalConfigEntity = internalConfigService.getInternalConfigByClient(client);
                ExternalConfigEntity externalConfigEntity = externalConfigService.getExternalConfigByClient(client);

                if (internalConfigEntity.isNotValid()) {
                    log.info("Значение версии клиента {} на сервере не задано, будет выполнено принудительное " +
                            "обновление конфигурации клиента", client.getName());
                    accumulatorService.increment(accumulatorService.getChannel("refresh"), AvailableMetrics.REFRESH_FORCE_TOTAL);
                    externalConfigService.forceRefresh(client);
                } else if (externalConfigEntity.isNotValid()) {
                    log.info("Значение версии клиента {} на клиенте не задано, будет выполнено принудительное " +
                            "обновление конфигурации клиента", client.getName());
                    accumulatorService.increment(accumulatorService.getChannel("refresh"), AvailableMetrics.REFRESH_FORCE_TOTAL);
                    externalConfigService.forceRefresh(client);
                } else if (!internalConfigEntity.getConfigVersion().equals(externalConfigEntity.getExternalConfigProperty().getValue())) {
                    log.info("Значение версии клиента {} на клиенте и на сервере не совпадают, будет выполнено " +
                            "принудительное обновление конфигурации клиента", client.getName());
                    accumulatorService.increment(accumulatorService.getChannel("refresh"), AvailableMetrics.REFRESH_FORCE_TOTAL);
                    externalConfigService.forceRefresh(client);
                } else {
                    log.info("Значение версии клиента {} на клиенте и на сервере совпадают, " +
                            "конфигурация клиента не нуждается в обновлении", client.getName());
                    accumulatorService.increment(accumulatorService.getChannel("refresh"), AvailableMetrics.REFRESH_UNNECESSARY_TOTAL);
                }

            } catch (InternalConfigServiceGenericException e) {
                log.error(e.getMessage());
                accumulatorService.increment(accumulatorService.getChannel("internal"), AvailableMetrics.INTERNAL_ERROR_SERVICE);
            } catch (ExternalConfigServiceGenericException e) {
                log.error(e.getMessage());
                accumulatorService.increment(accumulatorService.getChannel("external"), AvailableMetrics.EXTERNAL_ERROR_SERVICE);
            } catch (Throwable e) {
                log.error(e.getMessage());
                accumulatorService.increment(accumulatorService.getChannel("refresh"), AvailableMetrics.REFRESH_ERROR_UNRECOGNIZED);
            }
        }
    }


}
