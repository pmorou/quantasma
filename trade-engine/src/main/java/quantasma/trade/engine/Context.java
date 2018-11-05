package quantasma.trade.engine;

public interface Context {
    DataService getDataService();

    OrderService getOrderService();

    StrategyControl getStrategyControl();
}
