package quantasma.app.config.service.backtest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.ta4j.core.AnalysisCriterion;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CriterionsFactory {

    private final Map<String, AnalysisCriterion> criterionMap;

    @Autowired
    public CriterionsFactory(List<AnalysisCriterion> criterions) {
        criterionMap = criterions.stream()
                                 .collect(Collectors.toMap(criterion -> criterion.getClass().getSimpleName(),
                                                           criterion -> criterion));
    }

    public Set<String> available() {
        return criterionMap.keySet();
    }

    public AnalysisCriterion get(String name) {
        return criterionMap.get(name);
    }
}
