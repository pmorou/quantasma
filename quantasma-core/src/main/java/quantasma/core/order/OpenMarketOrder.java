package quantasma.core.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.ta4j.core.Order;

@Data
@AllArgsConstructor
public class OpenMarketOrder implements MarketOrder {
    private final String label;
    private final double volume;
    private final String symbol;
    private final Order.OrderType orderType;
}
