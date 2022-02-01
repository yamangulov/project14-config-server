package org.satel.eip.project14.config.server.domain.config.internal.entity;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "application")
@Data
public class Clients {

    String refreshDelayInMs;

    List<Client> clients;

    @Data
    public static class Client {
        private String name;
        private String protocol;
        private String url;
        private String user;
        private String password;

        public boolean isNotValid() {
            return name == null || protocol == null || url == null || user == null || password == null;
        }
    }
}
