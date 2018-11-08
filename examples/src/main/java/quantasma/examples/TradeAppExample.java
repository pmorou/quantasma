package quantasma.examples;

import org.ta4j.core.Strategy;
import quantasma.model.CandlePeriod;
import quantasma.trade.engine.BaseContext;
import quantasma.trade.engine.BaseTradeEngine;
import quantasma.trade.engine.Context;
import quantasma.trade.engine.GroupTimeSeriesDefinition;
import quantasma.trade.engine.NullOrderService;
import quantasma.trade.engine.TimeSeriesDefinition;
import quantasma.trade.engine.TradeEngine;

import java.time.ZonedDateTime;

public class TradeAppExample {
    public static void main(String[] args) {
        // Any strategy based on TradeStrategy class needs an Context object
        final Context context = new BaseContext.Builder()
                .withTimeSeries(
                        // You can define any number of symbols and corresponding time windows.
                        GroupTimeSeriesDefinition.of("EURUSD", "EURGBP")
                                                 .add(new TimeSeriesDefinition(CandlePeriod.M1, 100))
                                                 .add(new TimeSeriesDefinition(CandlePeriod.M5, 100)))
                .withOrderService(new NullOrderService()) // OrderService implementations integrated with external APIs
                .build();

        final TradeEngine tradeEngine = BaseTradeEngine.create(context);

        final Strategy rsiStrategy = RSIStrategy.build(context);
        // Only registered strategies are given market data
        context.getStrategyControl().register(rsiStrategy);

        // Example call on market price change
        tradeEngine.process("EURUSD", ZonedDateTime.now(), 1.13757, 1.13767);

        // Will fail silently as the symbol wasn't registered within time series definitions
        tradeEngine.process("EURJPY", ZonedDateTime.now(), 129.653, 129.663);
    }
}
