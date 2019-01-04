package quantasma.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import quantasma.app.service.StrategyService;
import quantasma.core.StrategyDescription;

import java.util.Set;

@RestController
@RequestMapping("api/strategy")
public class StrategyController {

    private final StrategyService strategyService;

    @Autowired
    public StrategyController(StrategyService strategyService) {
        this.strategyService = strategyService;
    }

    @GetMapping("all")
    public Set<StrategyDescription> all() {
        return strategyService.all();
    }

    @PatchMapping("activate/{id}")
    public void activate(@PathVariable Long id) {
        strategyService.activate(id);
    }

    @PatchMapping("deactivate/{id}")
    public void deactivate(@PathVariable Long id) {
        strategyService.deactivate(id);
    }
}
