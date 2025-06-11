package quantasma.core;

import lombok.extern.slf4j.Slf4j;
import org.ta4j.core.*;
import org.ta4j.core.num.Num;
import quantasma.core.analysis.parametrize.Parameterizable;
import quantasma.core.analysis.parametrize.Values;
import quantasma.core.timeseries.MainTimeSeries;
import quantasma.core.timeseries.ManualIndexTimeSeries;
import quantasma.core.timeseries.bar.OneSidedBar;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class TestManager<B extends OneSidedBar> {
    private final Set<ManualIndexTimeSeries> manualIndexTimeSeriesSet;
    private final MarketData<B> marketData;

    public TestManager(MarketData<B> marketData) {
        this.marketData = marketData;
        this.manualIndexTimeSeriesSet = marketData.allTimeSeries()
            .stream()
            .map(o -> (ManualIndexTimeSeries) o)
            .collect(Collectors.toSet());
    }

    public TradingRecord run(TradeStrategy tradeStrategy, Trade.TradeType tradeType) {
        return run(tradeStrategy, tradeType, getMainTimeSeries(tradeStrategy).getBeginIndex(), getMainTimeSeries(tradeStrategy).getEndIndex());
    }

    public MainTimeSeries getMainTimeSeries(TradeStrategy tradeStrategy) {
        return marketData.of(tradeStrategy.getTradeSymbol()).getMainTimeSeries();
    }

    private TradingRecord run(TradeStrategy tradeStrategy, Trade.TradeType tradeType, int startIndex, int finishIndex) {
        return runTest(new IterateOverTimeSeries(tradeStrategy), tradeType, startIndex, finishIndex);
    }

    private TradingRecord runTest(TradeStrategy tradeStrategy, Trade.TradeType tradeType, int startIndex, int finishIndex) {
        final MainTimeSeries mainTimeSeries = getMainTimeSeries(tradeStrategy);
        if (mainTimeSeries.isEmpty()) {
            throw new RuntimeException("Empty time series");
        }

        final int runBeginIndex = Math.max(startIndex, mainTimeSeries.getBeginIndex());
        final int runEndIndex = Math.min(finishIndex, mainTimeSeries.getEndIndex());

        log.trace("Running trade strategy (indexes: {} -> {}): {} (starting with {})", runBeginIndex, runEndIndex, tradeStrategy, tradeType);
        final TradingRecord tradingRecord = new BaseTradingRecord(tradeType);
        for (int i = runBeginIndex; i <= runEndIndex; i++) {
            if (tradeStrategy.shouldOperate(i, tradingRecord)) {
                tradingRecord.operate(i, mainTimeSeries.getBar(i).getClosePrice(), tradeStrategy.getAmount());
            }
        }

        if (!tradingRecord.isClosed()) {
            final int seriesMaxSize = Math.max(mainTimeSeries.getEndIndex() + 1, mainTimeSeries.getBarCount());
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
        public void perform() {
            strategy.perform();
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
        public void setUnstableBars(int unstableBars) {
            strategy.setUnstableBars(unstableBars);
        }

        @Override
        public int getUnstableBars() {
            return strategy.getUnstableBars();
        }

        @Override
        public boolean isUnstableAt(int index) {
            return strategy.isUnstableAt(index);
        }
    }
}
