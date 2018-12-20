package quantasma.integrations.event;

import lombok.Data;

@Data
public class OpenedPosition {
    private final String symbol;
    private final Direction direction;
    private final double amount;
    private final double price;
    private final double stopLoss;
    private final double takeProfit;
    private final double profitLossPips;
    private final double profitLoss;
}
