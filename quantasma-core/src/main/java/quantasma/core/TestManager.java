package quantasma.core;

import lombok.extern.slf4j.Slf4j;
import org.ta4j.core.BaseTradingRecord;
import org.ta4j.core.Order;
import org.ta4j.core.Rule;
import org.ta4j.core.Strategy;
import org.ta4j.core.TradingRecord;
import org.ta4j.core.num.DoubleNum;
import org.ta4j.core.num.Num;
import quantasma.core.timeseries.MainTimeSeries;
import quantasma.core.timeseries.ManualIndexTimeSeries;

import java.util.Set;

@Slf4j
public class TestManager {

    private final Set<ManualIndexTimeSeries> manualIndexTimeSeriesSet;
    private final MainTimeSeries sourceTimeSeries;

    public TestManager(TestMarketData testMarketData, String mainSymbol) {
        this.sourceTimeSeries = testMarketData.of(mainSymbol).getMainTimeSeries();
        this.manualIndexTimeSeriesSet = testMarketData.manualIndexTimeSeres();
    }

    public TradingRecord run(TradeStrategy tradeStrategy, Order.OrderType orderType) {
        return run(tradeStrategy, orderType, sourceTimeSeries.getBeginIndex(), sourceTimeSeries.getEndIndex());
    }

    private TradingRecord run(TradeStrategy tradeStrategy, Order.OrderType orderType, int startIndex, int finishIndex) {
        return runTest(new IterateOverTimeSeries(tradeStrategy), orderType, startIndex, finishIndex);
    }

    private TradingRecord runTest(TradeStrategy strategy, Order.OrderType orderType, int startIndex, int finishIndex) {
        int runBeginIndex = Math.max(startIndex, sourceTimeSeries.getBeginIndex());
        int runEndIndex = Math.min(finishIndex, sourceTimeSeries.getEndIndex());

        log.trace("Running strategy (indexes: {} -> {}): {} (starting with {})", runBeginIndex, runEndIndex, strategy, orderType);
        TradingRecord tradingRecord = new BaseTradingRecord(orderType);
        for (int i = runBeginIndex; i <= runEndIndex; i++) {
            if (strategy.shouldOperate(i, tradingRecord)) {
                tradingRecord.operate(i, sourceTimeSeries.getBar(i).getClosePrice(), strategy.getAmount());
            }
        }

        if (!tradingRecord.isClosed()) {
            int seriesMaxSize = Math.max(sourceTimeSeries.getEndIndex() + 1, sourceTimeSeries.getBarData().size());
            for (int i = runEndIndex + 1; i < seriesMaxSize; i++) {
                if (strategy.shouldOperate(i, tradingRecord)) {
                    tradingRecord.operate(i, sourceTimeSeries.getBar(i).getClosePrice(), strategy.getAmount());
                    break;
                }
            }
        }
        return tradingRecord;
    }

    /**
     * Strategy decorator, changes indexes when {@code shouldOperate} method is called.
     */
    private class IterateOverTimeSeries implements TradeStrategy {
        private final TradeStrategy strategy;

        private boolean running;

        private IterateOverTimeSeries(TradeStrategy strategy) {
            this.strategy = strategy;
        }

        @Override
        public boolean shouldOperate(int index, TradingRecord tradingRecord) {
            if (!running) {
                resetIndexes();
                running = true;
            }

            // set next index before processing starts
            nextIndex();
            return strategy.shouldOperate(index, tradingRecord);
        }

        private void nextIndex() {
            manualIndexTimeSeriesSet.forEach(ManualIndexTimeSeries::nextIndex);
        }

        private void resetIndexes() {
            manualIndexTimeSeriesSet.forEach(ManualIndexTimeSeries::resetIndexes);
        }

        @Override
        public Num getAmount() {
            // unique values imitating possibility of changing an amount between trades
            return DoubleNum.valueOf(Math.random());
        }

        // decorator methods below

        @Override
        public String getName() {
            return strategy.getName();
        }

        @Override
        public String getTradeSymbol() {
            return strategy.getTradeSymbol();
        }

        @Override
        public Rule getEntryRule() {
            return strategy.getEntryRule();
        }

        @Override
        public Rule getExitRule() {
            return strategy.getExitRule();
        }

        @Override
        public Strategy and(Strategy strategy) {
            return strategy.and(strategy);
        }

        @Override
        public Strategy or(Strategy strategy) {
            return strategy.or(strategy);
        }

        @Override
        public Strategy and(String name, Strategy strategy, int unstablePeriod) {
            return strategy.and(name, strategy, unstablePeriod);
        }

        @Override
        public Strategy or(String name, Strategy strategy, int unstablePeriod) {
            return strategy.or(name, strategy, unstablePeriod);
        }

        @Override
        public Strategy opposite() {
            return strategy.opposite();
        }

        @Override
        public void setUnstablePeriod(int unstablePeriod) {
            strategy.setUnstablePeriod(unstablePeriod);
        }

        @Override
        public int getUnstablePeriod() {
            return strategy.getUnstablePeriod();
        }

        @Override
        public boolean isUnstableAt(int index) {
            return strategy.isUnstableAt(index);
        }
    }
}
