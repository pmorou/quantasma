package quantasma.core;

public class BaseTradeEngine implements TradeEngine {

    private final StrategyControl strategyControl;
    private final OrderService orderService;
    private final DataService dataService;

    private BaseTradeEngine(Context context) {
        dataService = context.getDataService();
        orderService = context.getOrderService();
        strategyControl = context.getStrategyControl();
    }

    public static TradeEngine create(Context context) {
        return new BaseTradeEngine(context);
    }

    @Override
    public void process(Quote quote) {
        dataService.getMarketData().add(quote);
        strategyControl.activeStrategies().forEach(TradeStrategy::perform);
    }
}
