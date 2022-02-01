package org.satel.eip.project14.config.server.domain.config.internal;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.satel.eip.project14.config.server.domain.config.internal.entity.Clients;
import org.satel.eip.project14.config.server.domain.config.internal.entity.InternalConfigEntity;
import org.satel.eip.project14.config.server.domain.config.internal.exception.GetInternalConfigByClientException;
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

    @Value("${spring.cloud.config.server.native.searchLocations}")
    private String pathToConfigsFolder;

    public InternalConfigService(InternalConfigRepository internalConfigRepository) {
        this.internalConfigRepository = internalConfigRepository;
    }

    public InternalConfigEntity getInternalConfigByClient(@NonNull Clients.Client client) throws GetInternalConfigByClientException {
        if (client.getName() == null || "".concat(client.getName()).isEmpty() || "".concat(client.getName()).isBlank()) {
            throw new GetInternalConfigByClientException("Не задано имя клиента " + client.getName() +
                    "в файле конфигурации на сервере");
        }

        if (pathToConfigsFolder == null || "".concat(pathToConfigsFolder).isEmpty() || "".concat(pathToConfigsFolder).isBlank()) {
            throw new GetInternalConfigByClientException("Не задан путь к директории для хранения конфигураций " +
                    "клиентов на сервере");
        }

        String configPathString = (pathToConfigsFolder + client.getName()).replace("file:", "");


        Path configPath = Paths.get(configPathString);

        File file = new File(configPath.toUri());
        if (!file.exists() || !file.isFile() || !file.canRead()) {
            throw new GetInternalConfigByClientException("Не получен файл конфигурации клиента " + client.getName() +
                    "на сервере, проверьте существование файла, его корректность и права доступа");
        }

        try {
            String configContent = Files.readString(configPath);

            if (configContent == null || configContent.isEmpty() || configContent.isBlank()) {
                throw new GetInternalConfigByClientException("Не удалось корректно прочесть " +
                        "содержание файла конфигурации клиента " + client.getName() +
                        "на сервере, проверьте файл, возможно, он не заполнен");
            }

            return internalConfigRepository.getInternalConfigEntityByConfigContent(configContent);
        } catch (JsonProcessingException e) {
            throw new GetInternalConfigByClientException("Ошибка парсинга файла конфигурации клиента " + client.getName() +
                    "в объект InternalConfigEntity, файл должен быть корректно размечен по спецификации YAML");
        } catch (IOException e) {
            throw new GetInternalConfigByClientException("Неизвестная ошибка ввода-вывода при получении " +
                    "текущей конфигурации клиента " + client.getName());
        }
    }
}
