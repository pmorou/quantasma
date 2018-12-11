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
import quantasma.core.analysis.TradeScenario;
import quantasma.core.analysis.parametrize.Producer;
import quantasma.core.analysis.parametrize.Variable;
import quantasma.core.analysis.parametrize.Variables;
import quantasma.core.timeseries.MultipleTimeSeriesBuilder;
import quantasma.core.timeseries.ReflectionManualIndexTimeSeries;
import quantasma.core.timeseries.TimeSeriesDefinition;

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

        final Function<Variables, TradeStrategy> recipe = var -> {
            var._int("rsiPeriod").values(10, 14);
            var._int("rsiLowerBound").with(range(10, 40, 10));
            var._int("rsiUpperBound").with(range(90, 60, 10));
            var._String("tradeSymbol").with("EURUSD");
            return RSIStrategy.buildBullish(context, var.getParameters());
        };

        // Feed historical data by calling testMarketData.add()

        final TestManager testManager = new TestManager(testMarketData);
        final Producer<TradeStrategy> producer = Producer.from(recipe);
        while (producer.hasNext()) {
            final TradeStrategy tradeStrategy = producer.next();
            final TradingRecord result = testManager.run(tradeStrategy, Order.OrderType.BUY);
            // Proper criterion can be used now on the result
        }
        // end::ParametrizedBacktestExample[]
    }
}
