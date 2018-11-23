package quantasma.core;

import org.ta4j.core.Order;
import org.ta4j.core.Rule;
import org.ta4j.core.Strategy;
import org.ta4j.core.TimeSeries;
import org.ta4j.core.TradingRecord;
import quantasma.core.timeseries.ManualIndexTimeSeries;

import java.util.Set;

/**
 * Acts like an adapter to the {@code TimeSeriesManager} where all significant logic happens.
 */
public class TestManager {

    private final org.ta4j.core.TimeSeriesManager timeSeriesManager;
    private final Set<ManualIndexTimeSeries> manualIndexTimeSeriesSet;
    private final MutableNum orderAmountRef;
    private final TimeSeries sourceTimeSeries;

    public TestManager(TestMarketData testMarketData, String mainSymbol, MutableNum orderAmountRef) {
        this.sourceTimeSeries = testMarketData.of(mainSymbol).getReferenceTimeSeries().source();
        this.timeSeriesManager = new org.ta4j.core.TimeSeriesManager(sourceTimeSeries);
        this.manualIndexTimeSeriesSet = testMarketData.manualIndexTimeSeres();
        this.orderAmountRef = orderAmountRef;
    }

    public TradingRecord run(Strategy strategy, Order.OrderType orderType) {
        resetOrderAmount();
        return timeSeriesManager.run(new IterateOverTimeSeries(strategy), orderType, orderAmountRef, sourceTimeSeries.getBeginIndex(), sourceTimeSeries.getEndIndex());
    }

    private void resetOrderAmount() {
        orderAmountRef.mutate(0);
    }

    /**
     * Strategy decorator, changes indexes when {@code shouldOperate} method is called.
     */
    private class IterateOverTimeSeries implements Strategy {
        private final Strategy strategy;

        private boolean running;

        private IterateOverTimeSeries(Strategy strategy) {
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
        public String getName() {
            return strategy.getName();
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
