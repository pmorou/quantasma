package quantasma.core.analysis;

import lombok.Data;

import java.util.Map;

@Data
public class BacktestResult {
    private final Map<Object, Object> params;
    private final Map<String, String> criterions;
}