package quantasma.app.feature.data.historical.tester;

import quantasma.app.model.OhlcvBar;
import quantasma.core.Quote;

import java.time.ZoneOffset;
import java.util.function.Function;
import java.util.stream.Stream;

public class TestModeExtractorBidAsk implements TestModeExtractor {
    @Override
    public Function<OhlcvBar, Stream<Quote>> closePrices() {
        return ohlcvBar -> Stream.of(
                prepareClosePrice(ohlcvBar));
    }

    @Override
    public Function<OhlcvBar, Stream<Quote>> openPrices() {
        return ohlcvBar -> Stream.of(
                prepareOpenPrice(ohlcvBar));
    }

    @Override
    public Function<OhlcvBar, Stream<Quote>> openHighLowClosePrices() {
        return ohlcvBar -> Stream.of(
                prepareOpenPrice(ohlcvBar),
                prepareHighPrice(ohlcvBar),
                prepareLowPrice(ohlcvBar),
                prepareClosePrice(ohlcvBar));
    }

    private static Quote prepareOpenPrice(OhlcvBar ohlcvBar) {
        return Quote.bidAsk(
                ohlcvBar.getSymbol(),
                ohlcvBar.getDate().atZone(ZoneOffset.UTC),
                ohlcvBar.getBidOpen(),
                ohlcvBar.getAskOpen());
    }

    private static Quote prepareHighPrice(OhlcvBar ohlcvBar) {
        return Quote.bidAsk(
                ohlcvBar.getSymbol(),
                ohlcvBar.getDate().atZone(ZoneOffset.UTC),
                ohlcvBar.getBidHigh(),
                ohlcvBar.getAskHigh());
    }

    private static Quote prepareLowPrice(OhlcvBar ohlcvBar) {
        return Quote.bidAsk(
                ohlcvBar.getSymbol(),
                ohlcvBar.getDate().atZone(ZoneOffset.UTC),
                ohlcvBar.getBidLow(),
                ohlcvBar.getAskLow());
    }

    private static Quote prepareClosePrice(OhlcvBar ohlcvBar) {
        return Quote.bidAsk(
                ohlcvBar.getSymbol(),
                ohlcvBar.getDate().atZone(ZoneOffset.UTC),
                ohlcvBar.getBidClose(),
                ohlcvBar.getAskClose());
    }
}
