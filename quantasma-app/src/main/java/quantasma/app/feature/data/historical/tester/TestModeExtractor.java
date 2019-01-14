package quantasma.app.feature.data.historical.tester;

import quantasma.app.model.OhlcvBar;
import quantasma.core.Quote;

import java.util.function.Function;
import java.util.stream.Stream;

public interface TestModeExtractor {

    Function<OhlcvBar, Stream<Quote>> closePrices();

    Function<OhlcvBar, Stream<Quote>> openPrices();

    Function<OhlcvBar, Stream<Quote>> openHighLowClosePrices();

}
