package quantasma.trade.engine;

import org.ta4j.core.BaseBar;
import org.ta4j.core.num.NaN;
import org.ta4j.core.num.Num;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.function.Function;

public class NullSafeBaseBar extends BaseBar {
    public NullSafeBaseBar(Duration timePeriod, ZonedDateTime endTime, Function<Number, Num> numFunction) {
        super(timePeriod, endTime, numFunction);
    }

    @Override
    public Num getOpenPrice() {
        return nonNull(super.getOpenPrice());
    }

    @Override
    public Num getMinPrice() {
        return nonNull(super.getMinPrice());
    }

    @Override
    public Num getMaxPrice() {
        return nonNull(super.getMaxPrice());
    }

    @Override
    public Num getClosePrice() {
        return nonNull(super.getClosePrice());
    }

    private static Num nonNull(Num num) {
        return num == null ? NaN.NaN : num;
    }
//
//    @Override
//    public String toString() {
//        return String.format("{end time: %1s, close price: %2$f, open price: %3$f, min price: %4$f, max price: %5$f, volume: %6$f}",
//                             getEndTime().withZoneSameInstant(ZoneId.systemDefault()),
//                             safeToDouble(getClosePrice()),
//                             safeToDouble(getOpenPrice()),
//                             safeToDouble(getMinPrice()),
//                             safeToDouble(getMaxPrice()),
//                             safeToDouble(getVolume()));
//    }
//
//    private static double safeToDouble(Num num) {
//        return num == null ? Double.NaN : num.doubleValue();
//    }
//

}
