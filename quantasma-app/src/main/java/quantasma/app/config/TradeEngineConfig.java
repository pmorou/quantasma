package quantasma.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import quantasma.core.BarPeriod;
import quantasma.core.BaseContext;
import quantasma.core.BaseStrategyControl;
import quantasma.core.BaseTradeEngine;
import quantasma.core.Context;
import quantasma.core.NullOrderService;
import quantasma.core.OrderService;
import quantasma.core.StrategyControl;
import quantasma.core.TradeEngine;
import quantasma.core.timeseries.MultipleTimeSeriesBuilder;
import quantasma.core.timeseries.TimeSeriesDefinition;

@Configuration
public class TradeEngineConfig {

    @Bean
    public StrategyControl strategyControl() {
        return new BaseStrategyControl();
    }

    @Bean
    public OrderService orderService() {
        return new NullOrderService();
    }

    @Bean
    public Context context(StrategyControl strategyControl, OrderService orderService) {
        return BaseContext.Builder.builder()
                                  .withTimeSeries(MultipleTimeSeriesBuilder.basedOn(TimeSeriesDefinition.limited(BarPeriod.M1, 100))
                                                                           .symbols("EURUSD"))
                                  .withOrderService(orderService)
                                  .withStrategyControl(strategyControl)
                                  .build();
    }

    @Bean
    public TradeEngine tradeEngine(Context context) {
        return BaseTradeEngine.create(context);
    }
}
