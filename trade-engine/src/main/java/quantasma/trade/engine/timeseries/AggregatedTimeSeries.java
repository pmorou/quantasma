package quantasma.trade.engine.timeseries;

import org.ta4j.core.Bar;
import org.ta4j.core.BaseTimeSeries;
import org.ta4j.core.TimeSeries;
import quantasma.model.CandlePeriod;
import quantasma.trade.engine.DateUtils;

public class AggregatedTimeSeries extends BaseTimeSeries {
    private final TimeSeries sourceTimeSeries;
    private final CandlePeriod candlePeriod;

    public AggregatedTimeSeries(TimeSeries sourceTimeSeries, String name, CandlePeriod candlePeriod) {
        super(name);
        this.sourceTimeSeries = sourceTimeSeries;
        this.candlePeriod = candlePeriod;
    }

    @Override
    public Bar getFirstBar() {
        // avoid index manipulation
        return super.getBar(getBeginIndex());
    }

    @Override
    public Bar getLastBar() {
        // avoid index manipulation
        return super.getBar(getEndIndex());
    }

    @Override
    public Bar getBar(int index) {
        final Bar sourceBar = sourceTimeSeries.getBar(index);

        final Bar optimisticEstimate = super.getBar(optimisticIndexEstimate(index));
        final boolean isOptimisticEstimateCorrect = DateUtils.isInRange(sourceBar.getBeginTime(),
                                                                        sourceBar.getEndTime(),
                                                                        optimisticEstimate.getBeginTime(),
                                                                        optimisticEstimate.getEndTime(),
                                                                        true);
        if (isOptimisticEstimateCorrect) {
            return optimisticEstimate;
        }

        /*
           First N-period aggregated bar was closed before M-period source time series had N/M bars
         */
        final Bar correctedEstimate = super.getBar(optimisticIndexEstimate(index) + 1);
        final boolean isReEstimateCorrect = DateUtils.isInRange(sourceBar.getBeginTime(),
                                                                sourceBar.getEndTime(),
                                                                correctedEstimate.getBeginTime(),
                                                                correctedEstimate.getEndTime(),
                                                                true);
        if (isReEstimateCorrect) {
            return correctedEstimate;
        }

        throw new RuntimeException(String.format("Correct bar not found.\n"
                                                 + "SourceBar(begin:%s,end:%s) - OptimisticEstimate(begin:%s,end:%s) - OptimisticEstimate+1(begin:%s,end:%s)",
                                                 sourceBar.getBeginTime(), sourceBar.getEndTime(),
                                                 optimisticEstimate.getBeginTime(), optimisticEstimate.getEndTime(),
                                                 correctedEstimate.getBeginTime(), correctedEstimate.getEndTime()));
    }

    private int optimisticIndexEstimate(int index) {
        return (int) (index / candlePeriod.getPeriod().toMinutes());
    }
}
