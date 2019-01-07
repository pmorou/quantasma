package quantasma.app.feature.data.historical.tester.backtests;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.ta4j.core.AnalysisCriterion;
import org.ta4j.core.Order;
import quantasma.app.config.service.backtest.CriterionsFactory;
import quantasma.app.model.OhlcvBar;
import quantasma.app.service.HistoricalDataService;
import quantasma.core.BarPeriod;
import quantasma.core.BaseContext;
import quantasma.core.Context;
import quantasma.core.MarketData;
import quantasma.core.MarketDataBuilder;
import quantasma.core.Quote;
import quantasma.core.StructureDefinition;
import quantasma.core.TestManager;
import quantasma.core.TradeStrategy;
import quantasma.core.analysis.BacktestResult;
import quantasma.core.analysis.StrategyBacktest;
import quantasma.core.analysis.TradeScenario;
import quantasma.core.analysis.parametrize.Parameterizable;
import quantasma.core.analysis.parametrize.Producer;
import quantasma.core.analysis.parametrize.Variables;
import quantasma.core.timeseries.ReflectionManualIndexTimeSeries;
import quantasma.core.timeseries.TimeSeriesDefinition;
import quantasma.core.timeseries.bar.BidAskBar;
import quantasma.core.timeseries.bar.BidAskBarFactory;
import quantasma.examples.RSIStrategy;
import quantasma.examples.RSIStrategy.Parameter;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.TemporalAmount;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Slf4j
public class RSIBacktest implements StrategyBacktest {

    private static final String SYMBOL = "EURUSD";
    private static final BarPeriod BASE_PERIOD = BarPeriod.M1;

    private final HistoricalDataService historicalDataService;
    private final CriterionsFactory criterionsFactory;

    @Autowired
    public RSIBacktest(HistoricalDataService historicalDataService, CriterionsFactory criterionsFactory) {
        this.historicalDataService = historicalDataService;
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
    public List<BacktestResult> run(Map<String, Object[]> backtestParameters, List<String> analysisCriterions, LocalDateTime fromDate, TemporalAmount timeWindow) {
        final MarketData<BidAskBar> marketData = createMarketData();

        final Context context = new BaseContext.Builder()
                .withMarketData(marketData)
                .build();

        final Function<Variables<Parameter>, TradeStrategy> recipe = var -> {
            backtestParameters.forEach((key, value) ->
                                               Variables.addValues(var,
                                                                   Parameter.valueOf(key),
                                                                   value));
            return RSIStrategy.buildBullish(context, var.getParameterValues());
        };

        // implement strategies: close, open, 4 ticks ohlc
        historicalDataService.findBySymbolAndDateBetweenOrderByDate(SYMBOL, fromDate.toInstant(ZoneOffset.UTC), timeWindow)
                             .forEach(loadBars(marketData));

        final TestManager testManager = new TestManager<>(marketData);

        final List<TradeScenario> tradeScenarios = prepareTradeScenarios(recipe, testManager);

        return tradeScenarios.stream()
                             .map(gatherResult(analysisCriterions))
                             .collect(Collectors.toList());
    }

    private Function<TradeScenario, BacktestResult> gatherResult(List<String> analysisCriterions) {
        return tradeScenario -> {
            final List<AnalysisCriterion> criterions = analysisCriterions.stream()
                                                                         .map(criterionsFactory::get)
                                                                         .collect(Collectors.toList());
            final Map<String, String> calculatedCriterions = analyze(tradeScenario, criterions);
            return new BacktestResult((Map<Object, Object>) tradeScenario.getValues().getValuesByParameter(), calculatedCriterions);
        };
    }

    private static Map<String, String> analyze(TradeScenario tradeScenario, List<AnalysisCriterion> criterions) {
        final Map<String, String> calculatedCriterions = new HashMap<>();
        for (AnalysisCriterion criterion : criterions) {
            calculatedCriterions.put(criterion.getClass().getSimpleName(),
                                     criterion.calculate(tradeScenario.getTimeSeries(),
                                                         tradeScenario.getTradingRecord())
                                              .toString());
        }
        return calculatedCriterions;
    }

    private static List<TradeScenario> prepareTradeScenarios(Function<Variables<Parameter>, TradeStrategy> recipe, TestManager testManager) {
        return Producer.from(recipe)
                       .stream()
                       .map(tradeStrategy -> new TradeScenario(testManager.getMainTimeSeries(tradeStrategy).plainTimeSeries(),
                                                               tradeStrategy.getParameterValues(),
                                                               testManager.run(tradeStrategy, Order.OrderType.BUY)))
                       .collect(Collectors.toList());
    }

    private static Consumer<OhlcvBar> loadBars(MarketData marketData) {
        return ohlcvBar -> marketData.add(
                Quote.bidAsk(ohlcvBar.getSymbol(),
                             ohlcvBar.getDate().atZone(ZoneOffset.UTC),
                             ohlcvBar.getBidClose(),
                             ohlcvBar.getAskClose()));
    }

    private static MarketData<BidAskBar> createMarketData() {
        return MarketDataBuilder.basedOn(StructureDefinition.model(new BidAskBarFactory())
                                                            .resolution(TimeSeriesDefinition.unlimited(BASE_PERIOD)))
                                .symbols(SYMBOL)
                                .wrap(ReflectionManualIndexTimeSeries::wrap)
                                .build();
    }

}
