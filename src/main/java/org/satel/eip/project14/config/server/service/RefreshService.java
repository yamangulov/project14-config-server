package org.satel.eip.project14.config.server.service;

import lombok.extern.slf4j.Slf4j;
import org.satel.eip.project14.config.server.data.application.Clients;
import org.satel.eip.project14.config.server.exception.NotValidRefreshStatusException;
import org.satel.eip.project14.config.server.repo.RestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RefreshService {

    private final Clients clients;
    private final RestRepository restRepository;

    @Autowired
    public RefreshService(Clients clients, RestRepository restRepository) {
        this.clients = clients;
        this.restRepository = restRepository;
    }

    public void refreshOnCheckConfigVersion() {
        clients.getClients().forEach(client -> {
            try {
                restRepository.refresh(client);
            } catch (NotValidRefreshStatusException e) {
                log.error(e.getMessage());
                //TODO подключить кастомную метрику
            }
        });
    }

}
