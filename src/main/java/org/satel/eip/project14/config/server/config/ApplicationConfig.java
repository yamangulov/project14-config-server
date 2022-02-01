package org.satel.eip.project14.config.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
public class ApplicationConfig {
    @Bean
    public RestTemplate restTemplate() {return new RestTemplate();}

    @Bean
    public Executor forceRefreshExecutor() {
        return Executors.newSingleThreadScheduledExecutor();
    }
}
