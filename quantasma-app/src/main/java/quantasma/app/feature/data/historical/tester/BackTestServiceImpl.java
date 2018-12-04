package quantasma.app.feature.data.historical.tester;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ta4j.core.TimeSeries;
import org.ta4j.core.TradingRecord;
import org.ta4j.core.analysis.criteria.AverageProfitCriterion;
import org.ta4j.core.analysis.criteria.AverageProfitableTradesCriterion;
import org.ta4j.core.analysis.criteria.BuyAndHoldCriterion;
import org.ta4j.core.analysis.criteria.MaximumDrawdownCriterion;
import org.ta4j.core.analysis.criteria.NumberOfBarsCriterion;
import org.ta4j.core.analysis.criteria.NumberOfTradesCriterion;
import org.ta4j.core.analysis.criteria.ProfitLossCriterion;
import org.ta4j.core.analysis.criteria.RewardRiskRatioCriterion;
import org.ta4j.core.analysis.criteria.TotalProfitCriterion;
import org.ta4j.core.analysis.criteria.VersusBuyAndHoldCriterion;
import quantasma.core.TradeStrategy;
import quantasma.core.analysis.StrategyBacktest;
import quantasma.core.analysis.TradeScenario;
import quantasma.core.analysis.criterion.FinishDepositCriterion;

import java.time.ZonedDateTime;
import java.time.temporal.TemporalAmount;
import java.util.List;

@Service
@Slf4j
public class BackTestServiceImpl implements BackTestService {

    @Autowired
    private List<StrategyBacktest> backtests;

    @Override
    public void testOverPeriod(TradeStrategy strategy, ZonedDateTime from, TemporalAmount timeDuration) {
        final StrategyBacktest backtest = backtests.stream()
                                                   .filter(strategyBacktest -> strategyBacktest.strategy() == strategy.getClass())
                                                   .findFirst()
                                                   .orElseThrow(RuntimeException::new);

        final List<TradeScenario> tradeScenarios = backtest.run(from.toLocalDateTime(), timeDuration);

        tradeScenarios.forEach(tradeScenario -> {
            log.info("==Backtesting result==");
            log.info("Strategy [{}], Parameters [{}]", backtest.strategy().getSimpleName(), tradeScenario.getParameters());
            final TimeSeries timeSeries = tradeScenario.getTimeSeries();
            final TradingRecord tradingRecord = tradeScenario.getTradingRecord();
            log.info("average profitable trades: {}", new AverageProfitableTradesCriterion().calculate(timeSeries, tradingRecord));
            log.info("average profit: {}", new AverageProfitCriterion().calculate(timeSeries, tradingRecord));
            log.info("buy and hold: {}", new BuyAndHoldCriterion().calculate(timeSeries, tradingRecord));
            log.info("maximum drawdown: {}", new MaximumDrawdownCriterion().calculate(timeSeries, tradingRecord));
            log.info("number of bars: {}", new NumberOfBarsCriterion().calculate(timeSeries, tradingRecord));
            log.info("number of trades: {}", new NumberOfTradesCriterion().calculate(timeSeries, tradingRecord));
            log.info("profit/loss: {}", new ProfitLossCriterion().calculate(timeSeries, tradingRecord));
            log.info("reward risk ratio: {}", new RewardRiskRatioCriterion().calculate(timeSeries, tradingRecord));
            log.info("total profit: {}", new TotalProfitCriterion().calculate(timeSeries, tradingRecord));
            log.info("versus buy and hold: {}", new VersusBuyAndHoldCriterion(new BuyAndHoldCriterion()).calculate(timeSeries, tradingRecord));
            log.info("finish deposit: {}", new FinishDepositCriterion(100, 0.0001).calculate(timeSeries, tradingRecord));

            log.info("trades:");
            tradingRecord.getTrades()
                         .stream()
                         .map(trade ->
                                      String.format("open price: %s, close price: %s, p/l in pips: %.2f, open date: %s, close date: %s",
                                                    trade.getEntry().getPrice(),
                                                    trade.getExit().getPrice(),
                                                    new FinishDepositCriterion.ProfitLossPipsCalculator(0.0001).calculate(timeSeries, trade).doubleValue(),
                                                    timeSeries.getBar(trade.getEntry().getIndex()).getBeginTime(),
                                                    timeSeries.getBar(trade.getExit().getIndex()).getBeginTime()))
                         .forEach(System.out::println);
        });
    }
}
