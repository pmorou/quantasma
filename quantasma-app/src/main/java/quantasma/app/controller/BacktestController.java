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
import quantasma.app.model.FeedSymbolTicksRequest;
import quantasma.app.model.FeedSymbolTicksResponse;
import quantasma.app.model.ParameterDescription;
import quantasma.app.model.PushTicksSettings;
import quantasma.app.model.SymbolTickSummary;
import quantasma.app.model.SymbolTickSummaryResponse;
import quantasma.app.service.OhlcvTickService;
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
    private final OhlcvTickService tickService;
    private final HistoricalDataUpdater historicalDataUpdater;

    @Value("${service.historical-data.enabled}")
    private boolean isHistoricServiceEnabled;

    @Autowired
    public BacktestController(List<StrategyBacktest> backtestList,
                              CriterionsFactory criterionsFactory,
                              OhlcvTickService tickService,
                              HistoricalDataUpdater historicalDataUpdater) {
        this.backtestList = backtestList;
        this.criterionsFactory = criterionsFactory;
        this.tickService = tickService;
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
    public SymbolTickSummaryResponse tickSummary() {
        return new SymbolTickSummaryResponse(tickService.symbolsTickSummary()
                                                        .stream()
                                                        .collect(Collectors.groupingBy(SymbolTickSummary::getSymbol)));
    }

    @RequestMapping(value = "ticks", method = RequestMethod.PUT)
    public FeedSymbolTicksResponse feedSymbolTicks(@RequestBody FeedSymbolTicksRequest request) {
        if (!isHistoricServiceEnabled) {
            log.info("History service disabled");
            return FeedSymbolTicksResponse.declined();
        }
        historicalDataUpdater.update(new PushTicksSettings(request.getSymbol(),
                                                           request.getBarPeriod(),
                                                           request.fromDateAsUtc(),
                                                           request.toDateAsUtc()));
        log.info("Processing by historical service: [{}]", request);
        return FeedSymbolTicksResponse.accepted();
    }

}
