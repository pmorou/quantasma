package quantasma.core.timeseries;

import lombok.Data;
import quantasma.core.BarPeriod;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class TimeSeriesDefinition {
    private final BarPeriod barPeriod;
    private final int maxBarCount;

    private TimeSeriesDefinition(BarPeriod barPeriod, int maxBarCount) {
        this.barPeriod = barPeriod;
        this.maxBarCount = maxBarCount;
    }

    public static TimeSeriesDefinition limited(BarPeriod barPeriod, int maxBarCount) {
        return new TimeSeriesDefinition(barPeriod, maxBarCount);
    }

    public static TimeSeriesDefinition unlimited(BarPeriod barPeriod) {
        return limited(barPeriod, Integer.MAX_VALUE);
    }

    @Data
    public static class Group {
        private final Set<String> symbols;
        private final Set<TimeSeriesDefinition> timeSeriesDefinitions;

        private Group(String... symbol) {
            this.symbols = Arrays.stream(symbol).collect(Collectors.toSet());
            this.timeSeriesDefinitions = new HashSet<>();
        }

        public static Group of(String... symbol) {
            return new Group(symbol);
        }

        public Group add(TimeSeriesDefinition timeSeriesDefinition) {
            this.timeSeriesDefinitions.add(timeSeriesDefinition);
            return this;
        }
    }
}
