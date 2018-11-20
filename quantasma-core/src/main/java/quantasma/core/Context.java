package quantasma.core;

public interface Context {
    DataService getDataService();

    OrderService getOrderService();

    StrategyControl getStrategyControl();
}
