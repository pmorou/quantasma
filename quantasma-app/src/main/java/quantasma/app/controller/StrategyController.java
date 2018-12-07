package quantasma.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import quantasma.app.service.StrategyService;

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
    public Set<String> all() {
        return strategyService.all();
    }
}
