package quantasma.core.timeseries;

import org.ta4j.core.Bar;
import quantasma.core.timeseries.bar.BidAskBar;
import quantasma.core.timeseries.bar.factory.BarFactory;
import quantasma.core.timeseries.bar.factory.BidAskBarFactory;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

public class MultipleTimeSeriesBuilder<B extends Bar> {

    private final TimeSeriesDefinition baseTimeSeriesDefinition;
    private final Set<TimeSeriesDefinition.Group> aggregatedTimeSeriesDefinitions = new HashSet<>();
    private final Set<String> symbols = new HashSet<>();

    private BarFactory<B> barFactory;
    private UnaryOperator<UniversalTimeSeries<B>> wrapper = timeSeries -> timeSeries;

    private MultipleTimeSeriesBuilder(BarFactory<B> barFactory, TimeSeriesDefinition baseTimeSeriesDefinition) {
        this.barFactory = barFactory;
        this.baseTimeSeriesDefinition = baseTimeSeriesDefinition;
    }

    public static MultipleTimeSeriesBuilder<BidAskBar> basedOn(TimeSeriesDefinition timeSeriesDefinition) {
        return new MultipleTimeSeriesBuilder<>(new BidAskBarFactory(), timeSeriesDefinition);
    }

    public static <B extends Bar> MultipleTimeSeriesBuilder<B> basedOn(BarFactory<B> barFactory,
                                                                       TimeSeriesDefinition timeSeriesDefinition) {
        return new MultipleTimeSeriesBuilder<>(barFactory, timeSeriesDefinition);
    }

    public MultipleTimeSeriesBuilder<B> aggregate(TimeSeriesDefinition.Group definitions) {
        this.aggregatedTimeSeriesDefinitions.add(definitions);
        return this;
    }

    public MultipleTimeSeriesBuilder<B> symbols(String... symbols) {
        this.symbols.addAll(Arrays.asList(symbols));
        return this;
    }

    public MultipleTimeSeriesBuilder<B> wrap(UnaryOperator<UniversalTimeSeries<B>> wrapper) {
        this.wrapper = wrapper;
        return this;
    }

    public Collection<? extends MultipleTimeSeries<B>> build() {
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

        return baseTimeSeries.values();
    }
}
