package org.satel.eip.project14.config.server.domain.config.external;

import lombok.extern.slf4j.Slf4j;
import org.satel.eip.project14.config.server.domain.config.external.entity.ExternalConfigEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class ExternalConfigRepository {

    private RestTemplate restTemplate;

    @Autowired
    public ExternalConfigRepository(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<ExternalConfigEntity> getExternalConfigEntityByUrl(BasicAuthenticationInterceptor interceptor, String url) {
        restTemplate.getInterceptors().add(interceptor);
        ResponseEntity<ExternalConfigEntity> responseEntity = restTemplate.getForEntity(url, ExternalConfigEntity.class);
        restTemplate.getInterceptors().remove(interceptor);
        return responseEntity;
    }

    public ResponseEntity<String> forceRefresh(BasicAuthenticationInterceptor interceptor, String url) {
        restTemplate.getInterceptors().add(interceptor);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, new HttpEntity<>(headers), String.class);
        restTemplate.getInterceptors().remove(interceptor);
        return responseEntity;
    }
}
