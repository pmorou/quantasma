package quantasma.app.feature.data.historical.tester.backtests;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.ta4j.core.Order;
import org.ta4j.core.TimeSeries;
import org.ta4j.core.TradingRecord;
import org.ta4j.core.indicators.RSIIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.trading.rules.CrossedDownIndicatorRule;
import org.ta4j.core.trading.rules.CrossedUpIndicatorRule;
import quantasma.app.service.OhlcvTickService;
import quantasma.core.BarPeriod;
import quantasma.core.BaseContext;
import quantasma.core.Context;
import quantasma.core.TestManager;
import quantasma.core.TestMarketData;
import quantasma.core.TradeStrategy;
import quantasma.core.analysis.StrategyBacktest;
import quantasma.core.analysis.TradeScenario;
import quantasma.core.analysis.parametrize.Producer;
import quantasma.core.analysis.parametrize.Variable;
import quantasma.core.analysis.parametrize.Variables;
import quantasma.core.timeseries.MultipleTimeSeriesBuilder;
import quantasma.core.timeseries.ReflectionManualIndexTimeSeries;
import quantasma.core.timeseries.TimeSeriesDefinition;
import quantasma.examples.RSIStrategy;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.TemporalAmount;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import static quantasma.core.analysis.parametrize.Ints.range;

@Component
public class RSIBacktest implements StrategyBacktest {

    private static final String SYMBOL = "EURUSD";
    private static final BarPeriod BASE_PERIOD = BarPeriod.M1;

    private final OhlcvTickService tickService;

    @Autowired
    public RSIBacktest(OhlcvTickService tickService) {
        this.tickService = tickService;
    }

    @Override
    public Class<? extends TradeStrategy> strategy() {
        return RSIStrategy.class;
    }

    @Override
    public List<TradeScenario> run(LocalDateTime from, TemporalAmount window) {
        final TestMarketData testMarketData = createTestMarketData();

        final Context context = new BaseContext.Builder()
                .withMarketData(testMarketData)
                .build();

        final TimeSeries timeSeries = context.getDataService()
                                             .getMarketData()
                                             .of(SYMBOL)
                                             .getTimeSeries(BarPeriod.M1);

        final ClosePriceIndicator closePrice = new ClosePriceIndicator(timeSeries);

        final Function<Variables, TradeStrategy> recipe = var -> {
            final Variable<Integer> rsiPeriod = var._int("rsiPeriod").values(10, 14);
            final Variable<Integer> rsiLowerBound = var._int("rsiLowerBound").with(range(10, 40, 10));

            final RSIIndicator rsi = new RSIIndicator(closePrice, rsiPeriod.$());
            return new RSIStrategy.Builder<>(context,
                                             SYMBOL,
                                             new CrossedDownIndicatorRule(rsi, rsiLowerBound.$()),
                                             new CrossedUpIndicatorRule(rsi, 100 - rsiLowerBound.$()))
                    .withUnstablePeriod(rsiPeriod.$())
                    .build();
        };

        // implement strategies: close, open, 4 ticks ohlc
        tickService.findBySymbolAndDateBetweenOrderByDate(SYMBOL, from.toInstant(ZoneOffset.UTC), window)
                   .forEach(ohlcvTick -> testMarketData.add(ohlcvTick.getSymbol(),
                                                            ohlcvTick.getDate().atZone(ZoneOffset.UTC),
                                                            ohlcvTick.getBidClose(),
                                                            ohlcvTick.getAskClose()));

        final Producer<TradeStrategy> producer = Producer.from(recipe);
        final List<TradeScenario> result = new LinkedList<>();
        while (producer.hasNext()) {
            final TradingRecord tradingRecord = new TestManager(testMarketData).run(producer.next(), Order.OrderType.BUY);
            result.add(new TradeScenario(timeSeries,
                                         producer.getParameters(),
                                         tradingRecord));
        }
        return result;
    }

    private static TestMarketData createTestMarketData() {
        return new TestMarketData(
                MultipleTimeSeriesBuilder.basedOn(TimeSeriesDefinition.unlimited(BASE_PERIOD))
                                         .symbols(SYMBOL)
                                         .wrap(ReflectionManualIndexTimeSeries::wrap)
                                         .build());
    }

}
