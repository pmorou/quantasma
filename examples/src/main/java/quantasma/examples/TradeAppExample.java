package quantasma.examples;

import org.ta4j.core.Strategy;
import quantasma.model.CandlePeriod;
import quantasma.trade.engine.BaseContext;
import quantasma.trade.engine.BaseTradeEngine;
import quantasma.trade.engine.Context;
import quantasma.trade.engine.NullOrderService;
import quantasma.trade.engine.TradeEngine;
import quantasma.trade.engine.timeseries.GroupTimeSeriesDefinition;
import quantasma.trade.engine.timeseries.MultipleTimeSeriesBuilder;
import quantasma.trade.engine.timeseries.TimeSeriesDefinitionImpl;

import java.time.ZonedDateTime;

public class TradeAppExample {
    public static void main(String[] args) {
        // tag::tradeAppExample[]
        // Any strategy based on TradeStrategy class needs a Context object
        final Context context = new BaseContext.Builder()
                .withTimeSeries(
                        MultipleTimeSeriesBuilder.basedOn(
                                // Smallest accessible time window for all defined below symbols
                                new TimeSeriesDefinitionImpl(CandlePeriod.M1, 100))
                                                 .symbols("EURUSD", "EURGBP")
                                                 // You can define any number of additional time windows for above symbols
                                                 .aggregate(GroupTimeSeriesDefinition.of("EURUSD")
                                                                                     .add(new TimeSeriesDefinitionImpl(CandlePeriod.M5, 100))
                                                                                     .add(new TimeSeriesDefinitionImpl(CandlePeriod.M30, 100)))
                )
                // OrderService implementations integrate an app with external APIs
                .withOrderService(new NullOrderService())
                .build();

        final TradeEngine tradeEngine = BaseTradeEngine.create(context);

        final Strategy rsiStrategy = RSIStrategy.buildBullish(context);

        // Only registered strategies are given market data
        context.getStrategyControl().register(rsiStrategy);

        // Example call on market data change
        tradeEngine.process("EURUSD", ZonedDateTime.now(), 1.13757, 1.13767);

        // Will fail silently because the symbol wasn't registered within time series definitions
        tradeEngine.process("EURJPY", ZonedDateTime.now(), 129.653, 129.663);
        // end::tradeAppExample[]
    }
}
