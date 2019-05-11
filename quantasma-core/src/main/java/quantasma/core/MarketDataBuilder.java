package quantasma.core;

import quantasma.core.timeseries.BaseMultipleTimeSeries;
import quantasma.core.timeseries.MultipleTimeSeries;
import quantasma.core.timeseries.TimeSeriesDefinition;
import quantasma.core.timeseries.GenericTimeSeries;
import quantasma.core.timeseries.bar.BarFactory;
import quantasma.core.timeseries.bar.OneSidedBar;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

public class MarketDataBuilder<B extends OneSidedBar> {

    private final TimeSeriesDefinition baseTimeSeriesDefinition;
    private final Set<TimeSeriesDefinition.Group> aggregatedTimeSeriesDefinitions = new HashSet<>();
    private final Set<String> symbols = new HashSet<>();

    private BarFactory<B> barFactory;
    private UnaryOperator<GenericTimeSeries<B>> wrapper = timeSeries -> timeSeries;

    private MarketDataBuilder(StructureDefinition<B> structure) {
        this.barFactory = structure.getModel().getBarFactory();
        this.baseTimeSeriesDefinition = structure.getResolution().getTimeSeriesDefinition();
    }

    public static <B extends OneSidedBar> MarketDataBuilder<B> basedOn(StructureDefinition<B> structure) {
        return new MarketDataBuilder<>(structure);
    }

    public MarketDataBuilder<B> aggregate(TimeSeriesDefinition.Group definitions) {
        this.aggregatedTimeSeriesDefinitions.add(definitions);
        return this;
    }

    public MarketDataBuilder<B> symbols(String... symbols) {
        this.symbols.addAll(Arrays.asList(symbols));
        return this;
    }

    public MarketDataBuilder<B> wrap(UnaryOperator<GenericTimeSeries<B>> wrapper) {
        this.wrapper = wrapper;
        return this;
    }

    public MarketData<B> build() {
        final Map<String, MultipleTimeSeries<B>> baseTimeSeries =
            symbols.stream()
                .map(symbol -> BaseMultipleTimeSeries.create(symbol, baseTimeSeriesDefinition, barFactory, wrapper))
                .collect(Collectors.toMap(BaseMultipleTimeSeries::getSymbol, Function.identity()));

        for (TimeSeriesDefinition.Group groupDefinition : aggregatedTimeSeriesDefinitions) {
            for (String symbol : groupDefinition.getSymbols()) {
                for (TimeSeriesDefinition timeSeriesDefinition : groupDefinition.getTimeSeriesDefinitions()) {
                    if (isLteBaseBarPeriod(timeSeriesDefinition.getBarPeriod())) {
                        throw new IllegalArgumentException(String.format("[%s] bar period for symbol [%s] =< base [%s]",
                            timeSeriesDefinition,
                            symbol,
                            baseTimeSeriesDefinition));
                    }
                    if (!baseTimeSeries.containsKey(symbol)) {
                        throw new IllegalArgumentException(String.format("Cannot aggregate undefined symbol [%s]", symbol));
                    }
                    baseTimeSeries.compute(symbol,
                        (ignored, multipleTimeSeries) -> multipleTimeSeries.aggregate(timeSeriesDefinition));
                }
            }
        }

        return new MarketData<>(baseTimeSeries.values());
    }

    private boolean isLteBaseBarPeriod(BarPeriod barPeriod) {
        return barPeriod.getPeriod().compareTo(baseTimeSeriesDefinition.getBarPeriod().getPeriod()) <= 0;
    }
}
