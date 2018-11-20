package quantasma.core.order;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OpenMarketOrder {
    private final int volume;
    private final String symbol;
}
