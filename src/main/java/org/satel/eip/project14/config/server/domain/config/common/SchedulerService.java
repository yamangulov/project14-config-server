package org.satel.eip.project14.config.server.domain.config.common;

import lombok.extern.slf4j.Slf4j;
import org.satel.eip.project14.config.server.metrics.accumulator.AccumulatorService;
import org.satel.eip.project14.config.server.metrics.accumulator.entity.AvailableMetrics;
import org.satel.eip.project14.config.server.metrics.accumulator.entity.Metrics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.Executor;

@Service
@EnableScheduling
@Slf4j
public class SchedulerService implements SchedulingConfigurer {
    private final RefreshService refreshService;
    private final AccumulatorService accumulatorService;

    @Value("${application.refreshDelayInMs}")
    private int delay;

    private final Executor taskExecutor;

    @Autowired
    public SchedulerService(RefreshService refreshService, AccumulatorService accumulatorService, @Qualifier("forceRefreshExecutor") Executor taskExecutor) {
        this.refreshService = refreshService;
        this.accumulatorService = accumulatorService;
        this.taskExecutor = taskExecutor;
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(taskExecutor);
        // триггер отсчитывает время следующего запуска обновления клиентов config-server через delay ms после
        // окончания предыдущего успешного обновления или от текущего времени при указанной ошибке, простая аннотация
        // @Scheduled в методе refreshOnCheckConfigVersion в RefreshService считала бы время от начала предыдущего
        // запуска независимо от успешности его завершения,что может привести к утечке памяти при постоянной
        // ошибке запуска задачи по расписанию и медленной обработке обновления по какой-то причине
        taskRegistrar.addTriggerTask(
                refreshService::refreshOnCheckConfigVersion,
                triggerContext -> {
                    Optional<Date> lastCompletionTime = Optional.ofNullable(triggerContext.lastCompletionTime());
                    if (lastCompletionTime.equals(Optional.empty())) {
                        log.info("Не удалось получить предыдущее время запуска обновления клиентов config-server.\n" +
                                "Новый запуск обновления будет выполнен через {} ms от текущего времени.", delay);
                        accumulatorService.increment(accumulatorService.getChannel("scheduler"),
                                AvailableMetrics.SCHEDULER_ERROR_UNHANDLED);
                    }
                    Instant nextExecutionTime =
                            lastCompletionTime.orElseGet(Date::new).toInstant()
                                    .plusMillis(delay);
                    Date nextRefreshDate = Date.from(nextExecutionTime);
                    log.info("Следующее обновление клиентов config-server запланировано на дату {}", nextRefreshDate);
                    accumulatorService.increment(accumulatorService.getChannel("scheduler"),
                            AvailableMetrics.SCHEDULER_REFRESH_ATTEMPTS_TOTAL);
                    return nextRefreshDate;
                }
        );
    }
}
