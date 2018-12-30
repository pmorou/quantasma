package quantasma.core;

import quantasma.core.timeseries.BaseMultipleTimeSeries;
import quantasma.core.timeseries.MultipleTimeSeries;
import quantasma.core.timeseries.TimeSeriesDefinition;
import quantasma.core.timeseries.UniversalTimeSeries;
import quantasma.core.timeseries.bar.BidAskBar;
import quantasma.core.timeseries.bar.OneSidedBar;
import quantasma.core.timeseries.bar.BarFactory;
import quantasma.core.timeseries.bar.BidAskBarFactory;

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
    private UnaryOperator<UniversalTimeSeries<B>> wrapper = timeSeries -> timeSeries;

    private MarketDataBuilder(BarFactory<B> barFactory, TimeSeriesDefinition baseTimeSeriesDefinition) {
        this.barFactory = barFactory;
        this.baseTimeSeriesDefinition = baseTimeSeriesDefinition;
    }

    public static MarketDataBuilder<BidAskBar> basedOn(TimeSeriesDefinition timeSeriesDefinition) {
        return basedOn(new BidAskBarFactory(), timeSeriesDefinition);
    }

    public static <B extends OneSidedBar> MarketDataBuilder<B> basedOn(BarFactory<B> barFactory,
                                                                       TimeSeriesDefinition timeSeriesDefinition) {
        return new MarketDataBuilder<>(barFactory, timeSeriesDefinition);
    }

    public MarketDataBuilder<B> aggregate(TimeSeriesDefinition.Group definitions) {
        this.aggregatedTimeSeriesDefinitions.add(definitions);
        return this;
    }

    public MarketDataBuilder<B> symbols(String... symbols) {
        this.symbols.addAll(Arrays.asList(symbols));
        return this;
    }

    public MarketDataBuilder<B> wrap(UnaryOperator<UniversalTimeSeries<B>> wrapper) {
        this.wrapper = wrapper;
        return this;
    }

    public MarketData<B> build() {
        final Map<String, MultipleTimeSeries<B>> baseTimeSeries = symbols.stream()
                                                                         .map(symbol -> BaseMultipleTimeSeries.create(symbol, baseTimeSeriesDefinition, barFactory, wrapper))
                                                                         .collect(Collectors.toMap(BaseMultipleTimeSeries::getSymbol, Function.identity()));

        for (TimeSeriesDefinition.Group groupDefinition : aggregatedTimeSeriesDefinitions) {
            for (String symbol : groupDefinition.getSymbols()) {
                for (TimeSeriesDefinition timeSeriesDefinition : groupDefinition.getTimeSeriesDefinitions()) {
                    if (!baseTimeSeries.containsKey(symbol)) {
                        throw new RuntimeException(String.format("Cannot aggregate undefined symbol [%s]", symbol));
                    }
                    baseTimeSeries.compute(symbol, (ignored, multipleTimeSeries) -> multipleTimeSeries.aggregate(timeSeriesDefinition));
                }
            }
        }

        return new MarketData<>(baseTimeSeries.values());
    }
}
