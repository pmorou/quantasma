# Quantasma ![Build Status](https://travis-ci.com/pmorou/quantasma.svg?branch=master)

All-in-one algorithmic trading platform. Build your own backtested-strategy using Java, and execute along with support of continuous track and optimization mechanics.

Modules:

-   [quantasma-app]({quantasma-app/}): ready-to-use application based on below modules

-   [quantasma-core]({quantasma-core/}): trading library providing core mechanics

-   [quantasma-examples]({quantasma-examples/}): example usages

-   [quantasma-integrations]({quantasma-integrations/}): integrations with 3rd party APIs

**NOTE: Provided modules might solve your problems, however, they are still in development. Use at your own risk.**

These solutions at its core stands on top of the modern technical analysis library [ta4j](https://github.com/ta4j/ta4j) providing many additional features as:

-   generic bar implementations, eg. BID and ASK prices

-   user-defined multi-period time series

-   flexible market-aware strategies

-   programmable parametrized backtests

The aim is to provide any needed functionality to follow the ever-changing markets in the most efficient way.

# Getting Started

Maven repository support soon. Right now fork/clone this repository and start using anything you need.

## Requirements

&gt;= Java 9

Upgrade to Java 10 is comming soon.

Before project compilation few dependencies must be installed, it can be done through script.

    ./scripts/install_dependencies.sh

Compilation should run now smoothly.

    mvn clean compile

## Example Usage

In case you decide to create your own trading application its as simple as the following code.

``` java
final MarketData<BidAskBar> marketData =
        MarketDataBuilder.basedOn(// Smallest accessible bar resolution for all defined below symbols
                                  TimeSeriesDefinition.limited(BarPeriod.M1, 100))
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
                                                           parameterValues -> parameterValues
                                                                   // Strings/Enums are allowed
                                                                   .add(Parameter.TRADE_SYMBOL, "EURUSD")
                                                                   .add(Parameter.RSI_PERIOD, 14)
                                                                   .add(Parameter.RSI_LOWER_BOUND, 30)
                                                                   .add(Parameter.RSI_UPPER_BOUND, 70));

// Only registered strategies are given market data
context.getStrategyControl().register(rsiStrategy);

final TradeEngine tradeEngine = BaseTradeEngine.create(context);

// Example call on market data change
tradeEngine.process(Quote.bidAsk("EURUSD",
                                 ZonedDateTime.now(),
                                 1.13757,
                                 1.13767));

// Will fail silently because the symbol wasn't registered within time series definitions
tradeEngine.process(Quote.bidAsk("EURJPY",
                                 ZonedDateTime.now(),
                                 129.653,
                                 129.663));
```

Backtest parametrization:

``` java
    final MarketData<BidAskBar> marketData =
            MarketDataBuilder.basedOn(TimeSeriesDefinition.unlimited(BarPeriod.M1))
                             .symbols("EURUSD")
                             .aggregate(TimeSeriesDefinition.Group.of("EURUSD")
                                                                  .add(TimeSeriesDefinition.unlimited(BarPeriod.M5)))
                             .wrap(ReflectionManualIndexTimeSeries::wrap)
                             .build();

    final Context context = new BaseContext.Builder()
            .withMarketData(marketData)
            .build();

    final Function<Variables<Parameter>, TradeStrategy> recipe = var -> {
        var._int(Parameter.RSI_PERIOD).values(10, 14);
        var._int(Parameter.RSI_LOWER_BOUND).with(range(10, 40, 10));
        var._int(Parameter.RSI_UPPER_BOUND).with(range(90, 60, 10));
        var._String(Parameter.TRADE_SYMBOL).with("EURUSD");
        return RSIStrategy.buildBullish(context, var.getParameterValues());
    };

    // Feed historical data by calling marketData.add()

    final TestManager testManager = new TestManager<>(marketData);
    Producer.from(recipe)
            .stream()
            .forEach(tradeStrategy -> {
                final TradingRecord result = testManager.run(tradeStrategy, Order.OrderType.BUY);
                // Proper criterion can be used now on the result
            });
}
```
