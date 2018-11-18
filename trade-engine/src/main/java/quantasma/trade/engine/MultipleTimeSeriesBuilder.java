package quantasma.trade.engine;

import quantasma.model.CandlePeriod;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MultipleTimeSeriesBuilder {

    private final TimeSeriesDefinition baseTimeSeriesDefinition;
    private Set<GroupTimeSeriesDefinition> aggregatedTimeSeriesDefinitions = new HashSet<>();
    private Set<String> symbols = new HashSet<>();

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
        final CandlePeriod baseCandlePeriod = baseTimeSeriesDefinition.getCandlePeriod();
        final Map<String, BaseMultipleTimeSeries> baseTimeSeries = symbols.stream()
                                                                          .map(symbol -> BaseMultipleTimeSeries.create(symbol, baseTimeSeriesDefinition))
                                                                          .collect(Collectors.toMap(BaseMultipleTimeSeries::getInstrument, Function.identity()));
        final Map<String, AggregableTimeSeries> aggregableTimeSeries = prepareAggregables(baseCandlePeriod, baseTimeSeries);

        for (GroupTimeSeriesDefinition aggrDefinition : aggregatedTimeSeriesDefinitions) {
            for (String symbol : aggrDefinition.getSymbols()) {
                final AggregableTimeSeries sourceTimeSeries = aggregableTimeSeries.get(symbol);
                for (TimeSeriesDefinition timeSeriesDefinition : aggrDefinition.getTimeSeriesDefinitions()) {
                    baseTimeSeries.computeIfPresent(symbol, (ignored, baseMultipleTimeSeries) ->
                            baseMultipleTimeSeries.put(timeSeriesDefinition.getCandlePeriod(), sourceTimeSeries.aggregate(timeSeriesDefinition)));
                    if (!baseTimeSeries.containsKey(symbol)) {
                        throw new RuntimeException("inavlid symbol: " + symbol);
                    }
                }
            }
        }
        return baseTimeSeries.values();
    }

    private Map<String, AggregableTimeSeries> prepareAggregables(CandlePeriod baseCandlePeriod, Map<String, BaseMultipleTimeSeries> bases) {
        return bases.entrySet()
                    .stream()
                    .collect(Collectors.toMap(Map.Entry::getKey,
                                              entry -> new AggregableTimeSeriesImpl(entry.getValue().getTimeSeries(baseCandlePeriod))));
    }
}
