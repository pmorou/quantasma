package quantasma.app.controller;

import lombok.Data;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import quantasma.app.config.service.backtest.CriterionsFactory;
import quantasma.core.analysis.BacktestResult;
import quantasma.core.analysis.StrategyBacktest;
import quantasma.core.analysis.parametrize.Parameterizable;

import java.time.LocalDate;
import java.time.Period;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("backtest")
public class BacktestController {

    private final List<StrategyBacktest> backtestList;
    private final CriterionsFactory criterionsFactory;

    @Autowired
    public BacktestController(List<StrategyBacktest> backtestList, CriterionsFactory criterionsFactory) {
        this.backtestList = backtestList;
        this.criterionsFactory = criterionsFactory;
    }

    @RequestMapping("all")
    public List<BacktestScenario> all() {
        return backtestList.stream()
                           .map(backtest -> new BacktestScenario(
                                   backtest.getClass().getSimpleName(),
                                   backtest.strategy().getSimpleName(),
                                   Arrays.stream(backtest.parameterizables())
                                         .map(p -> new Parameter(
                                                 p.name(),
                                                 p.clazz().getSimpleName()))
                                         .collect(Collectors.toList())))
                           .collect(Collectors.toList());
    }

    @Data
    static class BacktestScenario {
        private final String name;
        private final String strategy;
        private final List<Parameter> parameters;
    }

    @Data
    static class Parameter {
        private final String name;
        private final String type;
    }

    @RequestMapping("{name}")
    public BacktestScenario get(@PathVariable String name) {
        return backtestList.stream()
                           .filter(strategyBacktest -> strategyBacktest.getClass().getSimpleName().equalsIgnoreCase(name))
                           .map(backtest -> new BacktestScenario(
                                   backtest.getClass().getSimpleName(),
                                   backtest.strategy().getSimpleName(),
                                   Arrays.stream(backtest.parameterizables())
                                         .map(p -> new Parameter(
                                                 p.name(),
                                                 p.clazz().getSimpleName()))
                                         .collect(Collectors.toList())))
                           .findFirst()
                           .orElseThrow(RuntimeException::new);
    }

    @RequestMapping(value = "{name}", method = RequestMethod.POST)
    public List<BacktestResult> test(@PathVariable String name, @RequestBody String json) {
        return backtestList.stream()
                           .filter(strategyBacktest -> strategyBacktest.getClass().getSimpleName().equalsIgnoreCase(name))
                           .findFirst()
                           .map(strategyBacktest -> {
                               // temporary impl
                               final JSONObject data = new JSONObject(json);
                               final JSONObject time = data.getJSONObject("time");

                               final List<String> crits = new LinkedList<>();
                               final JSONArray criterions = data.getJSONArray("criterions");
                               for (int i = 0; i < criterions.length(); i++) {
                                   final JSONObject obj = criterions.getJSONObject(i);
                                   crits.add(obj.getString("name"));
                               }

                               final Map<String, Set<Object>> params = new HashMap<>();
                               final JSONArray parameters = data.getJSONArray("parameters");
                               for (Parameterizable parameterizable : strategyBacktest.parameterizables()) {
                                   params.put(parameterizable.name(), new HashSet<>());

                                   for (int i = 0; i < parameters.length(); i++) {
                                       final JSONObject obj = parameters.getJSONObject(i);
                                       final String pname = obj.getString("name");
                                       if (parameterizable.name().equals(pname)) {
                                           final String pvalue = obj.getString("value");
                                           final Set<Object> vals = params.get(pname);

                                           final String[] split = pvalue.split(",");
                                           if (parameterizable.clazz() == String.class) {
                                               for (String s : split) {
                                                   vals.add(s.trim());
                                               }
                                           } else if (parameterizable.clazz() == Integer.class) {
                                               for (String s : split) {
                                                   vals.add(Integer.parseInt(s.trim()));
                                               }
                                           } else {
                                               throw new IllegalArgumentException(obj.toString());
                                           }
                                       }

                                   }
                               }

                               final Map<String, Object[]> mapped = params.entrySet()
                                                                           .stream()
                                                                           .collect(Collectors.toMap(Map.Entry::getKey, o -> o.getValue().toArray()));
                               return strategyBacktest.run(mapped,
                                                           crits,
                                                           LocalDate.parse(time.getString("from")).atStartOfDay(),
                                                           Period.parse(time.getString("window")));
                           })
                           .orElseThrow(RuntimeException::new);
    }

    @RequestMapping("criterions")
    public Set<String> criterions() {
        return criterionsFactory.available();
    }
}
