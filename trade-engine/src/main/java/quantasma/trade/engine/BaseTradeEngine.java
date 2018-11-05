package quantasma.trade.engine;

import java.time.ZonedDateTime;

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
    public void process(String symbol, ZonedDateTime date, double price) {
        dataService.add(symbol, date, price);
        final int latestBarIndex = dataService.lastBarIndex();
        strategyControl.getActiveStrategies().forEach(strategy -> {
            if (!strategy.shouldEnter(latestBarIndex))
                strategy.shouldExit(latestBarIndex);
        });
    }

}
