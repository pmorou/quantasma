package quantasma.app.config.service.backtest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.ta4j.core.AnalysisCriterion;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class CriterionsFactory {

    private final Map<String, AnalysisCriterion> criterionMap = new HashMap<>();

    @Autowired
    public void collectCriterions(List<AnalysisCriterion> criterions) {
        criterions.forEach(criterion -> criterionMap.put(criterion.getClass().getSimpleName(), criterion));
    }

    public Set<String> available() {
        return criterionMap.keySet();
    }

    public AnalysisCriterion get(String name) {
        return criterionMap.get(name);
    }
}
