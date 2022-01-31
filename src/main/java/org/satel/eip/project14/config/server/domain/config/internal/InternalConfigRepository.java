package org.satel.eip.project14.config.server.domain.config.internal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.satel.eip.project14.config.server.domain.config.internal.entity.InternalConfigEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class InternalConfigRepository {

    private final ObjectMapper objectMapper;


    @Autowired
    public InternalConfigRepository(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }


    public InternalConfigEntity getInternalConfigEntityByConfigContent(@NonNull String configContent) throws JsonProcessingException {
        ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());
        Object obj = yamlMapper.readValue(configContent, Object.class);
        return objectMapper.readValue(objectMapper.writeValueAsString(obj), InternalConfigEntity.class);
    }


}
