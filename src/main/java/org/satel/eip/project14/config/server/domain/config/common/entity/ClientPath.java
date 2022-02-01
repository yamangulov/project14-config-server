package org.satel.eip.project14.config.server.domain.config.common.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ClientPath {
    ACTUATOR_CONFIG_VERSION("/actuator/env/configVersion"),
    ACTUATOR_REFRESH("/actuator/refresh");

    private final String path;

}
