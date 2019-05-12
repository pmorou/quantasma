package quantasma.examples;

import org.ta4j.core.Order;
import org.ta4j.core.TradingRecord;
import quantasma.core.BarPeriod;
import quantasma.core.BaseContext;
import quantasma.core.Context;
import quantasma.core.MarketData;
import quantasma.core.MarketDataBuilder;
import quantasma.core.StructureDefinition;
import quantasma.core.TestManager;
import quantasma.core.TradeStrategy;
import quantasma.core.timeseries.ReflectionManualIndexTimeSeries;
import quantasma.core.timeseries.TimeSeriesDefinition;
import quantasma.core.timeseries.bar.BidAskBar;
import quantasma.core.timeseries.bar.BidAskBarFactory;
import quantasma.examples.RSIStrategy.Parameter;

public class BacktestExample {
    public static void main(String[] args) {
        // tag::BacktestExample[]
        final MarketData<BidAskBar> marketData =
            MarketDataBuilder.basedOn(StructureDefinition.model(new BidAskBarFactory())
                .resolution(TimeSeriesDefinition.unlimited(BarPeriod.M1)))
                .symbols("EURUSD")
                .aggregate(TimeSeriesDefinition.Group.of("EURUSD")
                    .add(TimeSeriesDefinition.unlimited(BarPeriod.M5)))
                .wrap(ReflectionManualIndexTimeSeries::wrap)
                .build();

        final Context context = new BaseContext.Builder()
            .withMarketData(marketData)
            .build();

        final TradeStrategy rsiStrategy = RSIBullishStrategy.build(context,
            parameterValues -> parameterValues
                .set(Parameter.TRADE_SYMBOL, "EURUSD")
                .set(Parameter.RSI_PERIOD, 14)
                .set(Parameter.RSI_LOWER_BOUND, 30)
                .set(Parameter.RSI_UPPER_BOUND, 70));

        // Feed historical data by calling marketData.add()

        final TestManager manager = new TestManager<>(marketData);
        final TradingRecord result = manager.run(rsiStrategy, Order.OrderType.BUY);
        // Proper criterion can be used now on the result
        // end::BacktestExample[]
    }
}
