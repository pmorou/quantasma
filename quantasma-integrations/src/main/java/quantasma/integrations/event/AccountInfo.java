package quantasma.integrations.event;

import lombok.Data;

@Data
public class AccountInfo {
    private final double equity;
    private final double balance;
    private final double positionsProfitLoss;
    private final double positionsAmount;
    private final double usedMargin;
    private final String currency;
    private final double leverage;
}
