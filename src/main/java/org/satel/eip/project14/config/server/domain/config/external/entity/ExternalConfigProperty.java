package org.satel.eip.project14.config.server.domain.config.external.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExternalConfigProperty {
    @JsonProperty("value")
    private String value;
}
