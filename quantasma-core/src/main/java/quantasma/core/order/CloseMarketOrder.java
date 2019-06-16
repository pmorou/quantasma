package quantasma.core.order;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class CloseMarketOrder implements MarketOrder {
    private final String label;
}
