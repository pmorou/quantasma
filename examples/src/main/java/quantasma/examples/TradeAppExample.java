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
        final Context context = new BaseContext.Builder()
                .withTimeSeries(
                        GroupTimeSeriesDefinition.of("EURUSD", "EURGBP")
                                                 .add(new TimeSeriesDefinition(CandlePeriod.M1, 100))
                                                 .add(new TimeSeriesDefinition(CandlePeriod.M5, 100)))
                .withOrderService(new NullOrderService()) // integration with broker's api
                .build();

        final TradeEngine tradeEngine = BaseTradeEngine.create(context);

        final Strategy rsiStrategy = RSIStrategy.build(context);
        context.getStrategyControl().register(rsiStrategy);

        tradeEngine.process("EURUSD", ZonedDateTime.now(), 1.14145);
    }
}
