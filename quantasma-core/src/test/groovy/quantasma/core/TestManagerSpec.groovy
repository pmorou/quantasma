package quantasma.core

import org.ta4j.core.Bar
import org.ta4j.core.Order
import org.ta4j.core.TradingRecord
import org.ta4j.core.num.DoubleNum
import quantasma.core.timeseries.MainTimeSeries
import quantasma.core.timeseries.ManualIndexTimeSeries
import quantasma.core.timeseries.MultipleTimeSeries
import spock.lang.Specification

class TestManagerSpec extends Specification {

    private def tradeStrategy
    private def bar
    private def mainTimeSeries
    private def multipleTimeSeries
    private def testMarketData

    def setup() {
        tradeStrategy = Mock(TradeStrategy, {
            getTradeSymbol() >> "symbol"
            shouldOperate(_, _) >> true
        })

        bar = Stub(Bar, {
            getClosePrice() >>> [DoubleNum.valueOf(0), DoubleNum.valueOf(1), DoubleNum.valueOf(2)]
        })

        mainTimeSeries = Stub(MainTimeSeries, {
            getBar(_) >> bar
            getBeginIndex() >> 0
            getEndIndex() >> 2
        })

        multipleTimeSeries = Stub(MultipleTimeSeries, {
            getMainTimeSeries() >> mainTimeSeries
        })

        testMarketData = Stub(TestMarketData, {
            of("symbol") >> multipleTimeSeries
            manualIndexTimeSeres() >> [Stub(ManualIndexTimeSeries)]
        })
    }

    def 'given 3 bars should check strategy 3 times and have both finish and unfinished trades with different amount objects'() {
        given:
        def testManager = new TestManager(testMarketData)

        when:
        def result = testManager.run(tradeStrategy, Order.OrderType.BUY)

        then:
        3 * tradeStrategy.shouldOperate({ (0..2).contains(it) }, _) >> true
        result.getTrades().size() == 1 // 1 finished trade
        result.getCurrentTrade().isOpened() // 1 unfinished trade
        uniqueAmountRefs(result).size() == 2 // 2 unique values collected from above trades
    }

    private static def uniqueAmountRefs(TradingRecord result) {
        (result.getTrades() + result.getCurrentTrade())
                .collect({ it.getEntry().getAmount() })

    }
}