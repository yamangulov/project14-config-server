package org.satel.eip.project14.config.server.repo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.extern.slf4j.Slf4j;
import org.satel.eip.project14.config.server.data.application.Clients;
import org.satel.eip.project14.config.server.data.client.ClientConfig;
import org.satel.eip.project14.config.server.data.server.ClientConfigOnServer;
import org.satel.eip.project14.config.server.exception.NotValidConfigStatusException;
import org.satel.eip.project14.config.server.exception.NotValidConfigValueException;
import org.satel.eip.project14.config.server.exception.NotValidRefreshStatusException;
import org.satel.eip.project14.config.server.service.ClientPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Repository
@Slf4j
public class RestRepository {
    private RestTemplate restTemplate;
    private final ObjectMapper mapper;

    @Value("${spring.cloud.config.server.native.searchLocations}")
    private String pathToClientConfigs;

    @Value("${application.refreshDelayInMs}")
    private int delay;

    @Autowired
    public RestRepository(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    // делать RestTemplate бином здесь нельзя из-за циклических зависимостей
    @PostConstruct
    private void setRestTemplate() {
        this.restTemplate = new RestTemplate();
    }


    public String getOnServerConfigVersion(Clients.Client client) {
        String uri = (pathToClientConfigs + client.getName()).replace("file:", "");
        Path configPath = Paths.get(uri);
        String configString = null;
        try {
            configString = convertYamlToJson(Files.readString(configPath));
        } catch (IOException e) {
            log.error("Ошибка при конвертации конфигурационного файла {} в Json формат", uri);
            //TODO подключить кастомную метрику
        }
        ClientConfigOnServer config = new ClientConfigOnServer();
        try {
            config = mapper.readValue(configString, ClientConfigOnServer.class);
        } catch (JsonProcessingException e) {
            log.error("Ошибка при чтении версии конфигурации клиента из файла {}", uri);
            //TODO подключить кастомную метрику
        }
        //TODO подключить кастомную метрику
        return config.getConfigVersion();
    }

    public String getOnClientConfigVersion(Clients.Client client) throws NotValidConfigStatusException, NotValidConfigValueException {
        BasicAuthenticationInterceptor interceptor = setAuthInterceptor(client);
        restTemplate.getInterceptors().add(interceptor);
        ResponseEntity<ClientConfig> responseEntity = restTemplate.getForEntity(getConfigRequest(client), ClientConfig.class);
        ClientConfig clientConfig = responseEntity.getBody();
        unsetAuthInterceptor(interceptor);
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            throw new NotValidConfigStatusException(client.getName(), responseEntity.getStatusCodeValue(), delay);
            //TODO подключить кастомную метрику
        };
        if (clientConfig == null || clientConfig.getProperty() == null || clientConfig.getProperty().getValue() == null) {
            throw new NotValidConfigValueException(client.getName(), delay);
            //TODO подключить кастомную метрику
        }
        //TODO подключить кастомную метрику
        return clientConfig.getProperty().getValue();
    }

    private boolean checkConfigVersion(Clients.Client client) throws NotValidConfigStatusException, NotValidConfigValueException {
        return getOnServerConfigVersion(client).equals(getOnClientConfigVersion(client));
    }

    private BasicAuthenticationInterceptor setAuthInterceptor(Clients.Client client) {
        return new BasicAuthenticationInterceptor(client.getUser(), client.getPassword());
    }

    private void unsetAuthInterceptor(BasicAuthenticationInterceptor interceptor) {
        restTemplate.getInterceptors().remove(interceptor);
    }

    public void refresh(Clients.Client client) throws NotValidRefreshStatusException {
        boolean forRefresh = true;
        try {
            forRefresh = checkConfigVersion(client);
        } catch (NotValidConfigStatusException | NotValidConfigValueException e) {
            log.error(e.getMessage());
            //TODO подключить кастомную метрику
        }
        if (!forRefresh) {
            log.info("Полученная от клиента {} версия конфигурации не совпадает с хранимой на сервере\n" +
                    "Будет произведено обновление версии клиента", client.getName());
            //TODO подключить кастомную метрику
            BasicAuthenticationInterceptor interceptor = setAuthInterceptor(client);
            restTemplate.getInterceptors().add(interceptor);
            String refreshRequest = client.getProtocol() + "://" + client.getUrl() + ClientPath.ACTUATOR_REFRESH.getPath();
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(refreshRequest, getHttpEntity(), String.class);
            unsetAuthInterceptor(interceptor);
            if (responseEntity.getStatusCode() != HttpStatus.OK) {
                throw new NotValidRefreshStatusException(client.getName(), responseEntity.getStatusCodeValue(), delay);
                //TODO подключить кастомную метрику
            };
        } else {
            log.info("Версия конфигурации клиента {} не нуждается в обновлении", client.getName());
            //TODO подключить кастомную метрику
        }
    }

    private String convertYamlToJson(String yaml) throws JsonProcessingException {
        ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());
        Object obj = yamlMapper.readValue(yaml, Object.class);
        return mapper.writeValueAsString(obj);
    }

    private HttpEntity<String> getHttpEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(headers);
    }

    private String getConfigRequest(Clients.Client client) {
        return client.getProtocol() + "://" + client.getUrl() + ClientPath.ACTUATOR_CONFIG_VERSION.getPath();
    }
}
