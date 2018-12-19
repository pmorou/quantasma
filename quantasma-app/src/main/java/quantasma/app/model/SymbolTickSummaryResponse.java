package quantasma.app.model;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class SymbolTickSummaryResponse {
    private final Map<String, List<SymbolTickSummary>> data;
}
