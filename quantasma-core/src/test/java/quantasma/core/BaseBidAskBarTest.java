package quantasma.core;

import org.junit.Test;
import org.ta4j.core.num.Num;
import org.ta4j.core.num.PrecisionNum;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

public class BaseBidAskBarTest {
    @Test
    public void afterAddingBidAndAskPrices4TimesShouldReturnCorrectOpenHighLowCloseValues() {
        // given
        final Function<Number, Num> func = PrecisionNum::valueOf;
        final BaseBidAskBar bar = new BaseBidAskBar(Duration.ofMinutes(1), ZonedDateTime.now(), func);

        // when
        bar.addPrice(func.apply(15), func.apply(25));
        bar.addPrice(func.apply(19), func.apply(29));
        bar.addPrice(func.apply(11), func.apply(21));
        bar.addPrice(func.apply(16), func.apply(26));

        // then
        assertThat(bar.getBidOpenPrice().doubleValue()).isEqualTo(15);
        assertThat(bar.getBidMaxPrice().doubleValue()).isEqualTo(19);
        assertThat(bar.getBidMinPrice().doubleValue()).isEqualTo(11);
        assertThat(bar.getBidClosePrice().doubleValue()).isEqualTo(16);
        assertThat(bar.getAskOpenPrice().doubleValue()).isEqualTo(25);
        assertThat(bar.getAskMaxPrice().doubleValue()).isEqualTo(29);
        assertThat(bar.getAskMinPrice().doubleValue()).isEqualTo(21);
        assertThat(bar.getAskClosePrice().doubleValue()).isEqualTo(26);
    }
}