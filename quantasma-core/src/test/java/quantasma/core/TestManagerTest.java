package quantasma.core;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.ta4j.core.Bar;
import org.ta4j.core.Order;
import org.ta4j.core.TradingRecord;
import org.ta4j.core.num.DoubleNum;
import org.ta4j.core.num.Num;
import quantasma.core.timeseries.MainTimeSeries;
import quantasma.core.timeseries.ManualIndexTimeSeries;
import quantasma.core.timeseries.MultipleTimeSeries;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.AnyOf.anyOf;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.intThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TestManagerTest {

    @Mock
    private TradeStrategy tradeStrategy;

    @Mock
    private TestMarketData testMarketData;

    @Mock
    private MultipleTimeSeries multipleTimeSeries;

    @Mock
    private MainTimeSeries mainTimeSeries;

    @Mock
    private Bar bar;

    @Mock
    private ManualIndexTimeSeries manualIndexTimeSeries;

    @Test
    public void given3BarsShouldCheckStrategy3TimesAndHaveBothFinishAndUnfinishedTradesWithDifferentAmountObjects() {
        // given
        when(testMarketData.of("symbol")).thenReturn(multipleTimeSeries);
        when(multipleTimeSeries.getMainTimeSeries()).thenReturn(mainTimeSeries);
        when(testMarketData.manualIndexTimeSeres()).thenReturn(Collections.singleton(manualIndexTimeSeries));
        when(mainTimeSeries.getBeginIndex()).thenReturn(0);
        when(mainTimeSeries.getEndIndex()).thenReturn(2);
        when(tradeStrategy.shouldOperate(intThat(anyOf(asList(is(0), is(1), is(2)))), anyObject())).thenReturn(true);
        when(mainTimeSeries.getBar(intThat(anyOf(asList(is(0), is(1), is(2)))))).thenReturn(bar);
        when(bar.getClosePrice()).thenReturn(DoubleNum.valueOf(0), DoubleNum.valueOf(1), DoubleNum.valueOf(2));
        final TestManager testManager = new TestManager(testMarketData, "symbol");

        // when
        final TradingRecord result = testManager.run(tradeStrategy, Order.OrderType.BUY);

        // then
        verify(tradeStrategy, times(3)).shouldOperate(anyInt(), anyObject());
        assertThat(result.getTrades()).hasSize(1); // 1 finished trade
        assertThat(result.getCurrentTrade().isOpened()).isTrue(); // 1 unfinished trade
        assertThat(uniqueAmountRefs(result)).hasSize(2); // 2 unique values collected from above trades
    }

    private static Set<Num> uniqueAmountRefs(TradingRecord result) {
        return Stream.concat(result.getTrades().stream(),
                             Stream.of(result.getCurrentTrade()))
                     .map(trade -> trade.getEntry().getAmount())
                     .collect(Collectors.toSet());
    }
}