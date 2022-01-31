package org.satel.eip.project14.config.server.domain.config.internal;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.satel.eip.project14.config.server.data.application.Clients;
import org.satel.eip.project14.config.server.domain.config.internal.entity.InternalConfigEntity;
import org.satel.eip.project14.config.server.domain.config.internal.exception.InternalConfigServiceGetInternalConfigByClientException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Service
public class InternalConfigService {

    private final InternalConfigRepository internalConfigRepository;

    @Value("${spring.cloud.config.server.native.pathToConfigsFolder}")
    private String pathToConfigsFolder;

    public InternalConfigService(InternalConfigRepository internalConfigRepository) {
        this.internalConfigRepository = internalConfigRepository;
    }

    public InternalConfigEntity getInternalConfigByClient(@NonNull Clients.Client client) throws InternalConfigServiceGetInternalConfigByClientException {
        if (client.getName() == null || "".concat(client.getName()).isEmpty() || "".concat(client.getName()).isBlank()) {
            throw new InternalConfigServiceGetInternalConfigByClientException("blah blah blah");
        }

        if (pathToConfigsFolder == null || "".concat(pathToConfigsFolder).isEmpty() || "".concat(pathToConfigsFolder).isBlank()) {
            throw new InternalConfigServiceGetInternalConfigByClientException("blah blah blah");
        }

        String configPathString = (pathToConfigsFolder + client.getName()).replace("file:", "");


        Path configPath = Paths.get(configPathString);

        File file = new File(configPath.toUri());
        if (!file.exists() || !file.isFile() || !file.canRead()) {
            throw new InternalConfigServiceGetInternalConfigByClientException("blah blah blah");
        }

        try {
            String configContent = Files.readString(configPath);

            if (configContent == null || configContent.isEmpty() || configContent.isBlank()) {
                throw new InternalConfigServiceGetInternalConfigByClientException("blah blah blah");
            }

            return internalConfigRepository.getInternalConfigEntityByConfigContent(configContent);
        } catch (JsonProcessingException e) {
            throw new InternalConfigServiceGetInternalConfigByClientException("blah blah blah");
        } catch (IOException e) {
            throw new InternalConfigServiceGetInternalConfigByClientException("blah blah blah");
        }
    }
}
