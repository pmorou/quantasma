package quantasma.examples;

import org.ta4j.core.Order;
import org.ta4j.core.TimeSeries;
import org.ta4j.core.TradingRecord;
import org.ta4j.core.indicators.RSIIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.trading.rules.CrossedDownIndicatorRule;
import org.ta4j.core.trading.rules.CrossedUpIndicatorRule;
import quantasma.core.BarPeriod;
import quantasma.core.BaseContext;
import quantasma.core.Context;
import quantasma.core.TestManager;
import quantasma.core.TestMarketData;
import quantasma.core.TradeStrategy;
import quantasma.core.analysis.parametrize.Producer;
import quantasma.core.analysis.parametrize.Variable;
import quantasma.core.timeseries.MultipleTimeSeriesBuilder;
import quantasma.core.timeseries.ReflectionManualIndexTimeSeries;
import quantasma.core.timeseries.TimeSeriesDefinition;

import java.util.Iterator;
import java.util.function.Function;

import static quantasma.core.analysis.parametrize.Ints.range;

public class ParametrizedBacktestExample {
    public static void main(String[] args) {
        // tag::ParametrizedBacktestExample[]
        final TestMarketData testMarketData = new TestMarketData(
                MultipleTimeSeriesBuilder.basedOn(TimeSeriesDefinition.unlimited(BarPeriod.M1))
                                         .symbols("EURUSD")
                                         .aggregate(TimeSeriesDefinition.Group.of("EURUSD")
                                                                              .add(TimeSeriesDefinition.unlimited(BarPeriod.M5)))
                                         .wrap(ReflectionManualIndexTimeSeries::wrap)
                                         .build());

        final Context context = new BaseContext.Builder()
                .withMarketData(testMarketData)
                .build();

        final TimeSeries timeSeries = context.getDataService().getMarketData().of("EURUSD").getTimeSeries(BarPeriod.M1);
        final ClosePriceIndicator closePrice = new ClosePriceIndicator(timeSeries);

        final Function<Producer, TradeStrategy> recipe = p -> {
            final Variable<Integer> rsiPeriod = p._int("rsiPeriod").values(10, 14);
            final Variable<Integer> rsiLowerBound = p._int("rsiLowerBound").with(range(10, 40, 10));

            final RSIIndicator rsi = new RSIIndicator(closePrice, rsiPeriod.$());
            return new RSIStrategy.Builder<>(context,
                                             "EURUSD",
                                             new CrossedDownIndicatorRule(rsi, rsiLowerBound.$()),
                                             new CrossedUpIndicatorRule(rsi, 100 - rsiLowerBound.$()))
                    .withUnstablePeriod(rsiPeriod.$())
                    .build();
        };

        // Feed historical data by calling testMarketData.add()

        final Iterator<TradeStrategy> iterator = Producer.instance().iterator(recipe);
        while (iterator.hasNext()) {
            final TradingRecord result = new TestManager(testMarketData).run(iterator.next(), Order.OrderType.BUY);
            // Proper criterion can be used now on the result
        }
        // end::ParametrizedBacktestExample[]
    }
}
