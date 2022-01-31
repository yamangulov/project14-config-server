package org.satel.eip.project14.config.server.data.server;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClientConfigOnServer {
    @JsonProperty("configVersion")
    private String configVersion;
}
