package quantasma.model;

import java.time.Duration;
import java.util.Arrays;

public enum CandlePeriod {
    M1("m1", Duration.ofMinutes(1)),
    M5("m5", Duration.ofMinutes(5)),
    M15("m15", Duration.ofMinutes(15)),
    M30("m30", Duration.ofMinutes(30)),
    H1("h1", Duration.ofHours(1)),
    H4("h4", Duration.ofHours(4)),
    D("d", Duration.ofDays(1));

    private final String periodCode;
    private final Duration duration;

    CandlePeriod(String periodCode, Duration duration) {
        this.periodCode = periodCode;
        this.duration = duration;
    }

    public static CandlePeriod map(String periodCode) {
        return Arrays.stream(values())
                     .filter(candlePeriod -> candlePeriod.getPeriodCode().equals(periodCode))
                     .findFirst()
                     .orElseThrow(RuntimeException::new);
    }

    public String getPeriodCode() {
        return periodCode;
    }

    public Duration getPeriod() {
        return duration;
    }
}
