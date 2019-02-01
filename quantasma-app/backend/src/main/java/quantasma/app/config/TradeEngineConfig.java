package quantasma.app.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import quantasma.core.BarPeriod;
import quantasma.core.BaseContext;
import quantasma.core.BaseTradeEngine;
import quantasma.core.Context;
import quantasma.core.InMemoryStrategyControl;
import quantasma.core.MarketDataBuilder;
import quantasma.core.OrderService;
import quantasma.core.StrategyControl;
import quantasma.core.StructureDefinition;
import quantasma.core.TradeEngine;
import quantasma.core.timeseries.TimeSeriesDefinition;
import quantasma.core.timeseries.bar.BidAskBarFactory;

@Configuration
@Slf4j
public class TradeEngineConfig {

    @Bean
    public StrategyControl strategyControl() {
        return new InMemoryStrategyControl();
    }

    @Bean
    public Context context(StrategyControl strategyControl, OrderService orderService) {
        return BaseContext.Builder.builder()
                                  .withMarketData(MarketDataBuilder.basedOn(StructureDefinition.model(new BidAskBarFactory())
                                                                                               .resolution(TimeSeriesDefinition.limited(BarPeriod.M1, 100)))
                                                                   .symbols("EURUSD")
                                                                   .build())
                                  .withOrderService(orderService)
                                  .withStrategyControl(strategyControl)
                                  .build();
    }

    @Bean
    public TradeEngine tradeEngine(Context context) {
        return BaseTradeEngine.create(context);
    }

}
