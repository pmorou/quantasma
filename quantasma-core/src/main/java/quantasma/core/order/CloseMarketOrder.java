package quantasma.core.order;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CloseMarketOrder implements MarketOrder {
    private final String label;
}
