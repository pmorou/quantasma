package quantasma.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import quantasma.app.service.StrategyService;
import quantasma.core.StrategyDescription;

import java.util.Set;

@RestController
@RequestMapping("strategy")
public class StrategyController {

    private final StrategyService strategyService;

    @Autowired
    public StrategyController(StrategyService strategyService) {
        this.strategyService = strategyService;
    }

    @RequestMapping("all")
    public Set<StrategyDescription> all() {
        return strategyService.all();
    }

    @RequestMapping("activate/{id}")
    public void activate(@PathVariable Long id) {
        strategyService.activate(id);
    }

    @RequestMapping("deactivate/{id}")
    public void deactivate(@PathVariable Long id) {
        strategyService.deactivate(id);
    }
}
