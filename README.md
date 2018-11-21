# Quantasma ![Build Status](https://travis-ci.com/pmorou/quantasma.svg?branch=master)

All-in-one algorithmic trading platform. Build your own backtested-strategy using Java, and execute along with support of continuous track and optimization modules.

# Getting Started

## Requirements

&gt;= Java 8

## Example Usage

Creating a trading application is as simple as the following code.

``` java
// Any strategy based on TradeStrategy class needs a Context object
final Context context = new BaseContext.Builder()
        .withTimeSeries(
                MultipleTimeSeriesBuilder.basedOn(
                        // Smallest accessible time window for all defined below symbols
                        new TimeSeriesDefinitionImpl(BarPeriod.M1, 100))
                                         .symbols("EURUSD", "EURGBP")
                                         // You can define any number of additional time windows for above symbols
                                         .aggregate(GroupTimeSeriesDefinition.of("EURUSD")
                                                                             .add(new TimeSeriesDefinitionImpl(BarPeriod.M5, 100))
                                                                             .add(new TimeSeriesDefinitionImpl(BarPeriod.M30, 100)))
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
```
