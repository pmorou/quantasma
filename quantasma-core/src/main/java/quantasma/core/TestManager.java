package quantasma.core;

import lombok.extern.slf4j.Slf4j;
import org.ta4j.core.BaseTradingRecord;
import org.ta4j.core.Order;
import org.ta4j.core.Rule;
import org.ta4j.core.Strategy;
import org.ta4j.core.TradingRecord;
import org.ta4j.core.num.Num;
import quantasma.core.analysis.parametrize.Parameterizable;
import quantasma.core.analysis.parametrize.Values;
import quantasma.core.timeseries.MainTimeSeries;
import quantasma.core.timeseries.ManualIndexTimeSeries;

import java.util.Set;

@Slf4j
public class TestManager {
    private final Set<ManualIndexTimeSeries> manualIndexTimeSeriesSet;
    private final TestMarketData testMarketData;

    public TestManager(TestMarketData testMarketData) {
        this.testMarketData = testMarketData;
        this.manualIndexTimeSeriesSet = testMarketData.manualIndexTimeSeres();
    }

    public TradingRecord run(TradeStrategy tradeStrategy, Order.OrderType orderType) {
        return run(tradeStrategy, orderType, getMainTimeSeries(tradeStrategy).getBeginIndex(), getMainTimeSeries(tradeStrategy).getEndIndex());
    }

    public MainTimeSeries getMainTimeSeries(TradeStrategy tradeStrategy) {
        return testMarketData.of(tradeStrategy.getTradeSymbol()).getMainTimeSeries();
    }

    private TradingRecord run(TradeStrategy tradeStrategy, Order.OrderType orderType, int startIndex, int finishIndex) {
        return runTest(new IterateOverTimeSeries(tradeStrategy), orderType, startIndex, finishIndex);
    }

    private TradingRecord runTest(TradeStrategy tradeStrategy, Order.OrderType orderType, int startIndex, int finishIndex) {
        final MainTimeSeries mainTimeSeries = getMainTimeSeries(tradeStrategy);
        if (mainTimeSeries.isEmpty()) {
            throw new RuntimeException("Empty time series");
        }

        final int runBeginIndex = Math.max(startIndex, mainTimeSeries.getBeginIndex());
        final int runEndIndex = Math.min(finishIndex, mainTimeSeries.getEndIndex());

        log.trace("Running trade strategy (indexes: {} -> {}): {} (starting with {})", runBeginIndex, runEndIndex, tradeStrategy, orderType);
        final TradingRecord tradingRecord = new BaseTradingRecord(orderType);
        for (int i = runBeginIndex; i <= runEndIndex; i++) {
            if (tradeStrategy.shouldOperate(i, tradingRecord)) {
                tradingRecord.operate(i, mainTimeSeries.getBar(i).getClosePrice(), tradeStrategy.getAmount());
            }
        }

        if (!tradingRecord.isClosed()) {
            final int seriesMaxSize = Math.max(mainTimeSeries.getEndIndex() + 1, mainTimeSeries.getBarData().size());
            for (int i = runEndIndex + 1; i < seriesMaxSize; i++) {
                if (tradeStrategy.shouldOperate(i, tradingRecord)) {
                    tradingRecord.operate(i, mainTimeSeries.getBar(i).getClosePrice(), tradeStrategy.getAmount());
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

        // decorator methods below

        @Override
        public Num getAmount() {
            return strategy.getAmount();
        }

        @Override
        public String getName() {
            return strategy.getName();
        }

        @Override
        public String getTradeSymbol() {
            return strategy.getTradeSymbol();
        }

        @Override
        public Values getParameterValues() {
            return strategy.getParameterValues();
        }

        @Override
        public Parameterizable[] parameterizables() {
            return new Parameterizable[0];
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
