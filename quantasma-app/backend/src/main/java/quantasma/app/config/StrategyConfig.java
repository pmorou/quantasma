package quantasma.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import quantasma.core.Context;
import quantasma.core.StrategyControl;
import quantasma.core.TradeStrategy;
import quantasma.core.analysis.parametrize.Values;
import quantasma.examples.RSIBearishStrategy;
import quantasma.examples.RSIBullishStrategy;
import quantasma.examples.RSIStrategy;

@Configuration
public class StrategyConfig {

    @Bean
    public TradeStrategy rsiBullishStrategy(StrategyControl strategyControl, Context context) {
        final Values<RSIStrategy.Parameter> parameterValues =
                Values.of(RSIStrategy.Parameter.class)
                      .set(RSIStrategy.Parameter.TRADE_SYMBOL, "EURUSD")
                      .set(RSIStrategy.Parameter.RSI_PERIOD, 14)
                      .set(RSIStrategy.Parameter.RSI_LOWER_BOUND, 30)
                      .set(RSIStrategy.Parameter.RSI_UPPER_BOUND, 70);
        final TradeStrategy strategy = RSIBullishStrategy.build(context, parameterValues);
        strategyControl.register(strategy);
        return strategy;
    }

    @Bean
    public TradeStrategy rsiBearishStrategy(StrategyControl strategyControl, Context context) {
        final Values<RSIStrategy.Parameter> parameterValues =
                Values.of(RSIStrategy.Parameter.class)
                      .set(RSIStrategy.Parameter.TRADE_SYMBOL, "EURUSD")
                      .set(RSIStrategy.Parameter.RSI_PERIOD, 14)
                      .set(RSIStrategy.Parameter.RSI_LOWER_BOUND, 30)
                      .set(RSIStrategy.Parameter.RSI_UPPER_BOUND, 70);
        final TradeStrategy strategy = RSIBearishStrategy.build(context, parameterValues);
        strategyControl.register(strategy);
        return strategy;
    }

}
