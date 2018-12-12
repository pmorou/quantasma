# Quantasma ![Build Status](https://travis-ci.com/pmorou/quantasma.svg?branch=master)

All-in-one algorithmic trading platform. Build your own backtested-strategy using Java, and execute along with support of continuous track and optimization modules.

Modules short-description:

-   [quantasma-app]({quantasma-app/}): ready-to-use platform

-   [quantasma-core]({quantasma-core/}): trading library providing core mechanisms

-   [quantasma-examples]({quantasma-examples/}): example usages

-   [quantasma-integrations]({quantasma-integrations/}): integrations with 3rd party APIs

Platform stands on top of [ta4j](https://github.com/ta4j/ta4j) providing additional features as:

-   bid and ask prices

-   multi-period time series

-   market-aware strategies

-   parametrized backtests

You can use above platform or build your own based on [quantasma-core]({quantasma-core/}) library.

The aim is to provide any needed functionality to follow the ever-changing markets in the most efficient way.

# Getting Started

## Requirements

&gt;= Java 8

## Example Usage

In case you decide to create your own trading application its as simple as the following code.

``` java
// Any strategy based on TradeStrategy interface needs a Context object
final Context context = new BaseContext.Builder()
        .withTimeSeries(
                MultipleTimeSeriesBuilder.basedOn(
                        // Smallest accessible time window for all defined below symbols
                        TimeSeriesDefinition.limited(BarPeriod.M1, 100))
                                         .symbols("EURUSD", "EURGBP")
                                         // You can define any number of additional time windows for above symbols
                                         .aggregate(TimeSeriesDefinition.Group.of("EURUSD")
                                                                              .add(TimeSeriesDefinition.limited(BarPeriod.M5, 100))
                                                                              .add(TimeSeriesDefinition.limited(BarPeriod.M30, 100)))
        )
        // OrderService implementations integrate an app with external APIs
        .withOrderService(new NullOrderService())
        .build();

final TradeStrategy rsiStrategy = RSIStrategy.buildBullish(context,
                                                           Values.of(Parameter.class)
                                                                 // String and Enum are allowed
                                                                 .add(Parameter.TRADE_SYMBOL, "EURUSD")
                                                                 .add(Parameter.RSI_PERIOD, 14)
                                                                 .add(Parameter.RSI_LOWER_BOUND, 30)
                                                                 .add(Parameter.RSI_UPPER_BOUND, 70));

// Only registered strategies are given market data
context.getStrategyControl().register(rsiStrategy);

final TradeEngine tradeEngine = BaseTradeEngine.create(context);

// Example call on market data change
tradeEngine.process("EURUSD", ZonedDateTime.now(), 1.13757, 1.13767);

// Will fail silently because the symbol wasn't registered within time series definitions
tradeEngine.process("EURJPY", ZonedDateTime.now(), 129.653, 129.663);
```
