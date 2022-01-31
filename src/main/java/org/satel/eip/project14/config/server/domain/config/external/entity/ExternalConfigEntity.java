package org.satel.eip.project14.config.server.domain.config.external.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExternalConfigEntity {
    @JsonProperty("property")
    private ExternalConfigProperty externalConfigProperty;

    public boolean isValid() {
        //TODO проверка сущностей
        return true;
    }
}
