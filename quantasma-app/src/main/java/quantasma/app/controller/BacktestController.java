package quantasma.app.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import quantasma.app.config.service.backtest.CriterionsFactory;
import quantasma.app.feature.data.historical.provider.HistoricalDataUpdater;
import quantasma.app.model.BacktestRequest;
import quantasma.app.model.BacktestScenario;
import quantasma.app.model.FeedHistoricalBarsRequest;
import quantasma.app.model.FeedHistoricalBarsResponse;
import quantasma.app.model.ParameterDescription;
import quantasma.app.model.FeedBarsSettings;
import quantasma.app.model.HistoricalDataSummary;
import quantasma.app.model.HistoricalDataSummaryResponse;
import quantasma.app.service.HistoricalDataService;
import quantasma.core.analysis.BacktestResult;
import quantasma.core.analysis.StrategyBacktest;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/backtest")
@Slf4j
public class BacktestController {
    private final List<StrategyBacktest> backtestList;
    private final CriterionsFactory criterionsFactory;
    private final HistoricalDataService historicalDataService;
    private final HistoricalDataUpdater historicalDataUpdater;

    @Value("${service.historical-data.enabled}")
    private boolean isHistoricServiceEnabled;

    @Autowired
    public BacktestController(List<StrategyBacktest> backtestList,
                              CriterionsFactory criterionsFactory,
                              HistoricalDataService historicalDataService,
                              HistoricalDataUpdater historicalDataUpdater) {
        this.backtestList = backtestList;
        this.criterionsFactory = criterionsFactory;
        this.historicalDataService = historicalDataService;
        this.historicalDataUpdater = historicalDataUpdater;
    }

    @RequestMapping("all")
    public List<BacktestScenario> all() {
        return backtestList.stream()
                           .map(toBacktestScenario())
                           .collect(Collectors.toList());
    }

    @RequestMapping("{name}")
    public BacktestScenario get(@PathVariable String name) {
        return backtestList.stream()
                           .filter(strategyBacktest -> strategyBacktest.getClass().getSimpleName().equalsIgnoreCase(name))
                           .map(toBacktestScenario())
                           .findFirst()
                           .orElseThrow(RuntimeException::new);
    }

    private static Function<StrategyBacktest, BacktestScenario> toBacktestScenario() {
        return backtest -> new BacktestScenario(
                backtest.getClass().getSimpleName(),
                backtest.strategy().getSimpleName(),
                Arrays.stream(backtest.parameterizables())
                      .map(p -> new ParameterDescription(
                              p.name(),
                              p.clazz().getSimpleName()))
                      .collect(Collectors.toList()));
    }

    @RequestMapping(value = "{name}", method = RequestMethod.POST)
    public List<BacktestResult> test(@PathVariable String name, @RequestBody BacktestRequest request) {
        return backtestList.stream()
                           .filter(strategyBacktest -> strategyBacktest.getClass().getSimpleName().equalsIgnoreCase(name))
                           .findFirst()
                           .map(strategyBacktest -> strategyBacktest.run(request.parameters(strategyBacktest.parameterizables()),
                                                                         request.criterionNames(),
                                                                         request.getTime().getFrom().atStartOfDay(),
                                                                         request.getTime().getWindowAsPeriod()))
                           .orElseThrow(RuntimeException::new);
    }

    @RequestMapping("criterions")
    public Set<String> criterions() {
        return criterionsFactory.available();
    }

    @RequestMapping("ticks/summary")
    public HistoricalDataSummaryResponse dataSummary() {
        return new HistoricalDataSummaryResponse(historicalDataService.dataSummary()
                                                                      .stream()
                                                                      .collect(Collectors.groupingBy(HistoricalDataSummary::getSymbol)));
    }

    @RequestMapping(value = "ticks", method = RequestMethod.PUT)
    public FeedHistoricalBarsResponse feedHistoricalBars(@RequestBody FeedHistoricalBarsRequest request) {
        if (!isHistoricServiceEnabled) {
            log.info("History service disabled");
            return FeedHistoricalBarsResponse.declined();
        }
        historicalDataUpdater.update(new FeedBarsSettings(request.getSymbol(),
                                                          request.getBarPeriod(),
                                                          request.fromDateAsUtc(),
                                                          request.toDateAsUtc()));
        log.info("Processing by historical service: [{}]", request);
        return FeedHistoricalBarsResponse.accepted();
    }

}
