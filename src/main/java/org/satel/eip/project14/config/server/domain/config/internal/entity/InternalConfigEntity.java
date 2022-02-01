package org.satel.eip.project14.config.server.domain.config.internal.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class InternalConfigEntity {
    @JsonProperty("configVersion")
    private String configVersion;

    public boolean isNotValid() {
        //сюда попадает либо строка, либо null из версии клиента на сервере,
        // другие ошибки отлавливаются ДО попадания процесса в этот класс
        return configVersion == null;
    }
}
