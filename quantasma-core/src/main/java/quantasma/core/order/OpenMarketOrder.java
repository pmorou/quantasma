package quantasma.core.order;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.ta4j.core.Order;

@Data
@RequiredArgsConstructor
public class OpenMarketOrder implements MarketOrder {
    private final String label;
    private final double volume;
    private final String symbol;
    private final Order.OrderType orderType;
}
