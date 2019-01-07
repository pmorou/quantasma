package quantasma.examples;

import quantasma.core.BarPeriod;
import quantasma.core.BaseContext;
import quantasma.core.BaseTradeEngine;
import quantasma.core.Context;
import quantasma.core.MarketData;
import quantasma.core.MarketDataBuilder;
import quantasma.core.NullOrderService;
import quantasma.core.Quote;
import quantasma.core.StructureDefinition;
import quantasma.core.TradeEngine;
import quantasma.core.TradeStrategy;
import quantasma.core.timeseries.TimeSeriesDefinition;
import quantasma.core.timeseries.bar.BidAskBar;
import quantasma.core.timeseries.bar.BidAskBarFactory;
import quantasma.examples.RSIStrategy.Parameter;

import java.time.ZonedDateTime;

public class TradeAppExample {
    public static void main(String[] args) {
        // tag::tradeAppExample[]
        final MarketData<BidAskBar> marketData =
                MarketDataBuilder.basedOn(StructureDefinition
                                                  // Global Bar implementation factory
                                                  .model(new BidAskBarFactory())
                                                  // Smallest accessible bar resolution for all defined below symbols
                                                  .resolution(TimeSeriesDefinition.limited(BarPeriod.M1, 100)))
                                 .symbols("EURUSD", "EURGBP")
                                 // You can define any number of additional bars resolutions for above symbols
                                 .aggregate(TimeSeriesDefinition.Group.of("EURUSD")
                                                                      .add(TimeSeriesDefinition.limited(BarPeriod.M5, 100))
                                                                      .add(TimeSeriesDefinition.limited(BarPeriod.M30, 100)))
                                 .build();

        // Any strategy based on TradeStrategy interface needs a Context object
        final Context context = new BaseContext.Builder()
                .withMarketData(marketData)
                // OrderService implementations integrate an app with external APIs
                .withOrderService(new NullOrderService())
                .build();

        final TradeStrategy rsiStrategy = RSIStrategy.buildBullish(context,
                                                                   values -> values
                                                                           // String or Enum (for safety) is allowed
                                                                           .set(Parameter.TRADE_SYMBOL, "EURUSD")
                                                                           .set(Parameter.RSI_PERIOD, 14)
                                                                           .set(Parameter.RSI_LOWER_BOUND, 30)
                                                                           .set(Parameter.RSI_UPPER_BOUND, 70));

        // Only registered strategies are given market data
        context.getStrategyControl().register(rsiStrategy);

        final TradeEngine tradeEngine = BaseTradeEngine.create(context);

        // Example call on market data change
        tradeEngine.process(Quote.bidAsk("EURUSD",
                                         ZonedDateTime.now(),
                                         1.13757,
                                         1.13767));

        // Fails silently because the symbol wasn't registered within time series definitions
        tradeEngine.process(Quote.bidAsk("EURJPY",
                                         ZonedDateTime.now(),
                                         129.653,
                                         129.663));
        // end::tradeAppExample[]
    }

}
