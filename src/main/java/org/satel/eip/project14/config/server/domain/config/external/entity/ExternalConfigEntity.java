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

    public boolean isNotValid() {
        //сюда попадает либо строка, либо null из версии клиента на клиенте,
        // другие ошибки отлавливаются ДО попадания процесса в этот класс
        return externalConfigProperty == null || externalConfigProperty.getValue() == null;
    }
}
