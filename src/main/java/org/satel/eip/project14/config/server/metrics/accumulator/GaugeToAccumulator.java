package org.satel.eip.project14.config.server.metrics.accumulator;

import io.micrometer.core.instrument.Gauge;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.satel.eip.project14.config.server.domain.generic.exception.GenericException;

import java.util.concurrent.atomic.DoubleAccumulator;

public class GaugeToAccumulator {

    @Getter
    @Setter
    private Gauge gauge;

    @Getter
    @Setter
    private DoubleAccumulator accumulator;

    private DoubleAccumulator scheduleCheckCounter = new DoubleAccumulator(Double::sum, 0L);

    private Integer interval;

    public GaugeToAccumulator(@NonNull Integer interval) {
        this.interval = interval;
    }

    private GaugeToAccumulator() {}

    public void scheduleCheck() throws GenericException {
        if (interval == null) {
            throw new GenericException("interval can't be null");
        }
        scheduleCheckCounter.accumulate(1L);
        int value = (int) scheduleCheckCounter.get();
        if (value == interval) {
            scheduleCheckCounter.reset();
            accumulator.reset();
            gauge.measure();
        }
    }

    public void increment() {
        accumulator.accumulate(1L);
        gauge.measure();
    }
}
