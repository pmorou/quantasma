# Quantasma ![Build Status](https://travis-ci.com/pmorou/quantasma.svg?branch=master)

All-in-one algorithmic trading platform. Build your own backtested-strategy using Java, and execute along with support of continuous track and optimization modules.

Library stands on top of [ta4j](https://github.com/ta4j/ta4j) providing additional features as:

-   bid and ask prices

-   multi-period time series

-   unlimited number of symbols

The aim is to provide any needed functionality to follow the ever-changing markets in the most efficient way.

# Getting Started

## Requirements

&gt;= Java 8

## Example Usage

Creating a trading application is as simple as the following code.

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

        final TradeEngine tradeEngine = BaseTradeEngine.create(context);

        final TradeStrategy rsiStrategy = RSIStrategy.buildBullish(context);

//        final BaseTradeStrategy name = new BaseTradeStrategy.Builder(context, "EURUSD", rsiStrategy.getEntryRule(), rsiStrategy.getExitRule())
//                .withName("name")
//                .withUnstablePeriod(12)
//                .build();
//
//        final MegaTradeStrategy build = new MegaTradeStrategy.Builder(context, "EURUSD", rsiStrategy.getEntryRule(), rsiStrategy.getExitRule())
//                .withName("another")
//                .withUnstablePeriod(99)
//                .withX()
//                .build();

        // Only registered strategies are given market data
        context.getStrategyControl().register(rsiStrategy);

        // Example call on market data change
        tradeEngine.process("EURUSD", ZonedDateTime.now(), 1.13757, 1.13767);

        // Will fail silently because the symbol wasn't registered within time series definitions
        tradeEngine.process("EURJPY", ZonedDateTime.now(), 129.653, 129.663);
```
