package quantasma.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import quantasma.app.model.BacktestRequest;
import quantasma.app.model.BacktestScenario;
import quantasma.app.service.BacktestService;
import quantasma.core.analysis.BacktestResult;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("api/backtest")
public class BacktestController {

    private final BacktestService backtestService;

    @Autowired
    public BacktestController(BacktestService backtestService) {
        this.backtestService = backtestService;
    }

    @GetMapping("all")
    public List<BacktestScenario> all() {
        return backtestService.all();
    }

    @GetMapping("{name}")
    public BacktestScenario get(@PathVariable String name) {
        return backtestService.get(name);
    }

    @PostMapping("{name}")
    public List<BacktestResult> test(@PathVariable String name, @RequestBody BacktestRequest request) {
        return backtestService.test(name, request);
    }

    @GetMapping("criterions")
    public Set<String> criterions() {
        return backtestService.criterions();
    }

}
