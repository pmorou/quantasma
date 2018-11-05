package quantasma.trade.engine;

import lombok.Data;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class GroupTimeSeriesDefinition {

    private final Set<String> symbols;
    private final Set<TimeSeriesDefinition> timeSeriesDefinitions;

    public GroupTimeSeriesDefinition(String... symbol) {
        this.symbols = Arrays.stream(symbol).collect(Collectors.toSet());
        this.timeSeriesDefinitions = new HashSet<>();
    }

    public static GroupTimeSeriesDefinition of(String symbol) {
        return new GroupTimeSeriesDefinition(symbol);
    }


    public static GroupTimeSeriesDefinition of(String... symbol) {
        return new GroupTimeSeriesDefinition(symbol);
    }

    public GroupTimeSeriesDefinition add(TimeSeriesDefinition timeSeriesDefinition) {
        this.timeSeriesDefinitions.add(timeSeriesDefinition);
        return this;
    }
}
