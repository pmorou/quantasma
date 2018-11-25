package quantasma.core.analysis.criterion;

import org.junit.Test;
import org.ta4j.core.BaseBar;
import org.ta4j.core.BaseTradingRecord;
import org.ta4j.core.Order;
import org.ta4j.core.num.DoubleNum;
import org.ta4j.core.num.PrecisionNum;
import quantasma.core.BarPeriod;
import quantasma.core.timeseries.BaseSymbolTimeSeries;

import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class FinishDepositCriterionTest {

    private final ZonedDateTime time = ZonedDateTime.now();

    @Test
    public void given1000DepositWhenClose2TradesEach100000LotShouldReturn1020Deposit() {
        // given
        final FinishDepositCriterion finishDepositCriterion = new FinishDepositCriterion(1000, 0.0001);
        final BaseSymbolTimeSeries timeSeries = createTimeSeriesWithBars();

        // when
        final BaseTradingRecord result = new BaseTradingRecord(Order.buyAt(1, timeSeries, DoubleNum.valueOf(100_000)),
                                                               Order.sellAt(2, timeSeries, DoubleNum.valueOf(100_000)),
                                                               Order.buyAt(4, timeSeries, DoubleNum.valueOf(100_000)),
                                                               Order.sellAt(6, timeSeries, DoubleNum.valueOf(100_000)));

        // then
        assertThat(finishDepositCriterion.calculate(timeSeries, result).doubleValue()).isEqualTo(1020.0);
    }

    @Test
    public void given1000DepositWhenClose2TradesEach10000LotShouldReturn1002Deposit() {
        // given
        final FinishDepositCriterion finishDepositCriterion = new FinishDepositCriterion(1000, 0.0001);
        final BaseSymbolTimeSeries timeSeries = createTimeSeriesWithBars();

        // when
        final BaseTradingRecord result = new BaseTradingRecord(Order.buyAt(1, timeSeries, DoubleNum.valueOf(10_000)),
                                                               Order.sellAt(2, timeSeries, DoubleNum.valueOf(10_000)),
                                                               Order.buyAt(4, timeSeries, DoubleNum.valueOf(10_000)),
                                                               Order.sellAt(6, timeSeries, DoubleNum.valueOf(10_000)));

        // then
        assertThat(finishDepositCriterion.calculate(timeSeries, result).doubleValue()).isEqualTo(1002.0);
    }

    @Test
    public void given1000DepositWhenClose2TradesEach20000LotShouldReturn1004Deposit() {
        // given
        final FinishDepositCriterion finishDepositCriterion = new FinishDepositCriterion(1000, 0.0001);
        final BaseSymbolTimeSeries timeSeries = createTimeSeriesWithBars();

        // when
        final BaseTradingRecord result = new BaseTradingRecord(Order.buyAt(1, timeSeries, DoubleNum.valueOf(20_000)),
                                                               Order.sellAt(2, timeSeries, DoubleNum.valueOf(20_000)),
                                                               Order.buyAt(4, timeSeries, DoubleNum.valueOf(20_000)),
                                                               Order.sellAt(6, timeSeries, DoubleNum.valueOf(20_000)));

        // then
        assertThat(finishDepositCriterion.calculate(timeSeries, result).doubleValue()).isEqualTo(1004.0);
    }

    @Test
    public void given1000DepositWhenClose2TradesEach10LotShouldReturn1000Point002Deposit() {
        // given
        final FinishDepositCriterion finishDepositCriterion = new FinishDepositCriterion(1000, 0.0001);
        final BaseSymbolTimeSeries timeSeries = createTimeSeriesWithBars();

        // when
        final BaseTradingRecord result = new BaseTradingRecord(Order.buyAt(1, timeSeries, DoubleNum.valueOf(10)),
                                                               Order.sellAt(2, timeSeries, DoubleNum.valueOf(10)),
                                                               Order.buyAt(4, timeSeries, DoubleNum.valueOf(10)),
                                                               Order.sellAt(6, timeSeries, DoubleNum.valueOf(10)));

        // then
        assertThat(finishDepositCriterion.calculate(timeSeries, result).doubleValue()).isEqualTo(1000.002);
    }

    private BaseSymbolTimeSeries createTimeSeriesWithBars() {
        final BaseSymbolTimeSeries timeSeries = new BaseSymbolTimeSeries.Builder("symbol", BarPeriod.M1).build();
        timeSeries.addBar(createM1Bar(0, "1.0000"));
        timeSeries.addBar(createM1Bar(1, "1.0000")); // open #1
        timeSeries.addBar(createM1Bar(2, "1.0001")); // close #1
        timeSeries.addBar(createM1Bar(3, "1.0001"));
        timeSeries.addBar(createM1Bar(4, "1.0001")); // open #2
        timeSeries.addBar(createM1Bar(5, "1.0002"));
        timeSeries.addBar(createM1Bar(6, "1.0002")); // close #2
        timeSeries.addBar(createM1Bar(7, "1.0002"));
        return timeSeries;
    }

    private BaseBar createM1Bar(int rollMinutes, String closePrice) {
        final BaseBar baseBar = new BaseBar(BarPeriod.M1.getPeriod(), time.plusMinutes(rollMinutes), PrecisionNum::valueOf);
        baseBar.addPrice(PrecisionNum.valueOf(closePrice));
        return baseBar;
    }
}