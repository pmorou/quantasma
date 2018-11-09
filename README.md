# Quantasma ![Build Status](https://travis-ci.com/pmorou/quantasma.svg?branch=master)

All-in-one algorithmic trading platform.

# Getting Started

## Requirements

&gt;= Java 8

## Example Usage

Creating a trading application is as simple as the following code.

``` java
// Any strategy based on TradeStrategy class needs a Context object
final Context context = new BaseContext.Builder()
        .withTimeSeries(
                // You can define any number of symbols and corresponding time windows
                GroupTimeSeriesDefinition.of("EURUSD", "EURGBP")
                                         .add(new TimeSeriesDefinition(CandlePeriod.M1, 100))
                                         .add(new TimeSeriesDefinition(CandlePeriod.M5, 100)))
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
