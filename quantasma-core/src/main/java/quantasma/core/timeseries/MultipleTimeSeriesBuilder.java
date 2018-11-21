package quantasma.core.timeseries;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MultipleTimeSeriesBuilder {

    private final TimeSeriesDefinition baseTimeSeriesDefinition;
    private final Set<GroupTimeSeriesDefinition> aggregatedTimeSeriesDefinitions = new HashSet<>();
    private final Set<String> symbols = new HashSet<>();

    private MultipleTimeSeriesBuilder(TimeSeriesDefinition baseTimeSeriesDefinition) {
        this.baseTimeSeriesDefinition = baseTimeSeriesDefinition;
    }

    public static MultipleTimeSeriesBuilder basedOn(TimeSeriesDefinition timeSeriesDefinition) {
        return new MultipleTimeSeriesBuilder(timeSeriesDefinition);
    }

    public MultipleTimeSeriesBuilder aggregate(GroupTimeSeriesDefinition definitions) {
        this.aggregatedTimeSeriesDefinitions.add(definitions);
        return this;
    }

    public MultipleTimeSeriesBuilder symbols(String... symbols) {
        this.symbols.addAll(Arrays.asList(symbols));
        return this;
    }

    public Collection<? extends MultipleTimeSeries> build() {
        final Map<String, MultipleTimeSeries> baseTimeSeries = symbols.stream()
                                                                      .map(symbol -> BaseMultipleTimeSeries.create(symbol, baseTimeSeriesDefinition))
                                                                      .collect(Collectors.toMap(BaseMultipleTimeSeries::getInstrument, Function.identity()));

        for (GroupTimeSeriesDefinition aggrDefinition : aggregatedTimeSeriesDefinitions) {
            for (String symbol : aggrDefinition.getSymbols()) {
                for (TimeSeriesDefinition timeSeriesDefinition : aggrDefinition.getTimeSeriesDefinitions()) {
                    if (!baseTimeSeries.containsKey(symbol)) {
                        throw new RuntimeException(String.format("Cannot aggregate undefined symbol [%s]", symbol));
                    }
                    baseTimeSeries.compute(symbol, (ignored, multipleTimeSeries) -> multipleTimeSeries.aggregate(timeSeriesDefinition));
                }
            }
        }

        return baseTimeSeries.values();
    }

}
