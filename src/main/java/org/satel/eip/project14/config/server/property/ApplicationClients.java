package org.satel.eip.project14.config.server.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "application")
@Data
public class ApplicationClients {

    int refreshDelay;

    List<Client> clients;

    @Data
    public static class Client {
        private String name;
        private String url;
        private String username;
        private String userpassword;
    }

}
