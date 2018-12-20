package quantasma.core;

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
        dataService.getMarketData().add(symbol, date, price);
        final int latestBarIndex = dataService.getMarketData().lastBarIndex();
        strategyControl.activeStrategies().forEach(strategy -> {
            if (!strategy.shouldEnter(latestBarIndex))
                strategy.shouldExit(latestBarIndex);
        });
    }

    @Override
    public void process(String symbol, ZonedDateTime date, double bid, double ask) {
        dataService.getMarketData().add(symbol, date, bid, ask);
        final int latestBarIndex = dataService.getMarketData().lastBarIndex();
        strategyControl.activeStrategies().forEach(strategy -> {
            if (!strategy.shouldEnter(latestBarIndex))
                strategy.shouldExit(latestBarIndex);
        });
    }

    @Override
    public void process(Quote quote) {
        process(quote.getSymbol(), quote.getTime(), quote.getBid(), quote.getAsk());
    }
}
