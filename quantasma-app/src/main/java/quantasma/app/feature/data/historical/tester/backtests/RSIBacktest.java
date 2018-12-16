package quantasma.app.feature.data.historical.tester.backtests;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.ta4j.core.AnalysisCriterion;
import org.ta4j.core.Order;
import quantasma.app.config.service.backtest.CriterionsFactory;
import quantasma.app.service.OhlcvTickService;
import quantasma.core.BarPeriod;
import quantasma.core.BaseContext;
import quantasma.core.Context;
import quantasma.core.TestManager;
import quantasma.core.TestMarketData;
import quantasma.core.TradeStrategy;
import quantasma.core.analysis.BacktestResult;
import quantasma.core.analysis.StrategyBacktest;
import quantasma.core.analysis.TradeScenario;
import quantasma.core.analysis.parametrize.Parameterizable;
import quantasma.core.analysis.parametrize.Producer;
import quantasma.core.analysis.parametrize.Variables;
import quantasma.core.timeseries.MultipleTimeSeriesBuilder;
import quantasma.core.timeseries.ReflectionManualIndexTimeSeries;
import quantasma.core.timeseries.TimeSeriesDefinition;
import quantasma.examples.RSIStrategy;
import quantasma.examples.RSIStrategy.Parameter;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.TemporalAmount;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Slf4j
public class RSIBacktest implements StrategyBacktest {

    private static final String SYMBOL = "EURUSD";
    private static final BarPeriod BASE_PERIOD = BarPeriod.M1;

    private final OhlcvTickService tickService;
    private final CriterionsFactory criterionsFactory;

    @Autowired
    public RSIBacktest(OhlcvTickService tickService, CriterionsFactory criterionsFactory) {
        this.tickService = tickService;
        this.criterionsFactory = criterionsFactory;
    }

    @Override
    public Class<? extends TradeStrategy> strategy() {
        return RSIStrategy.class;
    }

    @Override
    public Parameterizable[] parameterizables() {
        return RSIStrategy.Parameter.values();
    }

    @Override
    public List<BacktestResult> run(Map<String, Object[]> parameters, List<String> criterions, LocalDateTime from, TemporalAmount window) {
        final TestMarketData testMarketData = createTestMarketData();

        final Context context = new BaseContext.Builder()
                .withMarketData(testMarketData)
                .build();

        final Function<Variables<Parameter>, TradeStrategy> recipe = var -> {
            parameters.forEach((key, value) ->
                                       Variables.addValues(var,
                                                           Parameter.valueOf(key),
                                                           value));
            return RSIStrategy.buildBullish(context, var.getParameterValues());
        };

        // implement strategies: close, open, 4 ticks ohlc
        tickService.findBySymbolAndDateBetweenOrderByDate(SYMBOL, from.toInstant(ZoneOffset.UTC), window)
                   .forEach(ohlcvTick -> testMarketData.add(ohlcvTick.getSymbol(),
                                                            ohlcvTick.getDate().atZone(ZoneOffset.UTC),
                                                            ohlcvTick.getBidClose(),
                                                            ohlcvTick.getAskClose()));

        final TestManager testManager = new TestManager(testMarketData);
        final List<TradeScenario> tradeScenarios = Producer.from(recipe)
                                                           .stream()
                                                           .map(tradeStrategy -> new TradeScenario(testManager.getMainTimeSeries(tradeStrategy),
                                                                                                   tradeStrategy.getParameterValues(),
                                                                                                   testManager.run(tradeStrategy, Order.OrderType.BUY)))
                                                           .collect(Collectors.toList());

        return tradeScenarios.stream()
                             .map(tradeScenario -> {
                                 final List<AnalysisCriterion> criterionas = criterions.stream()
                                                                                       .map(criterionsFactory::get)
                                                                                       .collect(Collectors.toList());

                                 final Map<String, String> res = new HashMap<>();
                                 for (AnalysisCriterion criteriona : criterionas) {
                                     res.put(criteriona.getClass().getSimpleName(),
                                             criteriona.calculate(tradeScenario.getTimeSeries(),
                                                                  tradeScenario.getTradingRecord())
                                                       .toString());
                                 }


                                 return new BacktestResult((Map<Object, Object>) tradeScenario.getValues().getValuesByParameter(), res);
                             })
                             .collect(Collectors.toList());
    }

    private static TestMarketData createTestMarketData() {
        return new TestMarketData(
                MultipleTimeSeriesBuilder.basedOn(TimeSeriesDefinition.unlimited(BASE_PERIOD))
                                         .symbols(SYMBOL)
                                         .wrap(ReflectionManualIndexTimeSeries::wrap)
                                         .build());
    }

}
