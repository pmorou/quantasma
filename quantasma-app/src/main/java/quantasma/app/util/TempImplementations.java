package quantasma.app.util;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestBody;
import quantasma.core.analysis.BacktestResult;
import quantasma.core.analysis.StrategyBacktest;
import quantasma.core.analysis.parametrize.Parameterizable;

import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Class contains temporary non-invasive implementations, expected to reimplement
 */
public class TempImplementations {
    public static Function<StrategyBacktest, List<BacktestResult>> calculateBacktestResult(@RequestBody String json) {
        return strategyBacktest -> {
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
        };
    }
}
