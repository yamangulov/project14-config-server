package org.satel.eip.project14.config.server.metrics.accumulator.entity;

import java.util.Arrays;

public enum AvailableMetrics {
    SCHEDULER_ERROR_UNHANDLED("_scheduler_error.get_last_refresh_time", "tag", "Число ошибок при получении предыдущего времени запуска цикла обновления клиентов config server"),
    SCHEDULER_REFRESH_ATTEMPTS_TOTAL("_scheduler_refresh_attempts_total", "tag", "Число запусков цикла обновления клиентов config server"),
    REFRESH_FORCE_TOTAL("_refresh_force_running_total", "tag", "Число запусков обновления отдельных клиентов config server"),
    REFRESH_UNNECESSARY_TOTAL("_refresh_running_unnecessary_total", "tag", "Число проверок, когда обновление клиента config server не понадобилось"),
    INTERNAL_ERROR_SERVICE("_internal_error_service", "tag", "Число ошибок при обращении к InternalConfigService"),
    EXTERNAL_ERROR_SERVICE("_external_error_service", "tag", "Число ошибок при обращении к ExternalConfigService"),
    REFRESH_ERROR_UNRECOGNIZED("_refresh_error_unrecognized", "tag", "Число нераспознанных ошибок при запуске обновления клиента config server");

    private final String metric;
    private final String tag;
    private final String description;


    AvailableMetrics(String metric, String tag, String description) {
        this.metric = metric;
        this.tag = tag;
        this.description = description;
    }

    public String getMetric() {
        return metric;
    }

    public String getTag() {
        return tag;
    }

    public String getDescription() {
        return description;
    }

    public static AvailableMetrics fromString(String text) {
        return Arrays.stream(AvailableMetrics.values())
                .filter(type -> type.name().equalsIgnoreCase(text))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("No constant with text " + text + " found"));
    }


}
