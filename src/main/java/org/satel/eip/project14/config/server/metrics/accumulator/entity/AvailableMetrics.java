package org.satel.eip.project14.config.server.metrics.accumulator.entity;

import java.util.Arrays;

public enum AvailableMetrics {
    SCHEDULER_ERROR_UNHANDLED(".scheduler.error.get_last_refresh_time", "tag", "Число ошибок при получении предыдущего времени запуска обновления клиентов config server"),
    SCHEDULER_REFRESH_ATTEMPTS_TOTAL(".scheduler.refresh_attempts_total", "tag", "Число попыток запуска обновления клиентов config server"),
    EXTERNAL_ERROR_HTTP(".external.error.http", "tag", "Число ошибок при обращении к внешнему сервису"),
    INTERNAL_ERROR_UNHANDLED(".internal.error.unhandled", "tag", "Число сообщений не обработанных из за внутренней ошибки");

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
