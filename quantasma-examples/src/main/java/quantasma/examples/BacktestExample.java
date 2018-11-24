package quantasma.examples;

import org.ta4j.core.Order;
import org.ta4j.core.TradingRecord;
import quantasma.core.BarPeriod;
import quantasma.core.BaseContext;
import quantasma.core.Context;
import quantasma.core.TestManager;
import quantasma.core.TestMarketData;
import quantasma.core.TradeStrategy;
import quantasma.core.timeseries.MultipleTimeSeriesBuilder;
import quantasma.core.timeseries.ReflectionManualIndexTimeSeries;
import quantasma.core.timeseries.TimeSeriesDefinition;

public class BacktestExample {
    public static void main(String[] args) {
        // tag::BacktestExample[]
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

        final TradeStrategy rsiStrategy = RSIStrategy.buildBullish(context);

        // Feed historical data by calling testMarketData.add()

        final TestManager manager = new TestManager(testMarketData, "EURUSD");
        final TradingRecord result = manager.run(rsiStrategy, Order.OrderType.BUY);
        // Proper criterion can be used now on the result
        // end::BacktestExample[]
    }
}
