package quantasma.app.util;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAmount;
import java.util.Calendar;
import java.util.Collection;
import java.util.stream.DoubleStream;

public class Util {

    private final static ZoneId DEFAULT_ZONE_ID = ZoneOffset.UTC;

    public static ZonedDateTime convertToZonedDateTime(Calendar date) {
        return date.toInstant().atZone(DEFAULT_ZONE_ID);
    }

    public static ZonedDateTime convertToZonedDateTime(Instant date) {
        return date.atZone(DEFAULT_ZONE_ID);
    }

    // TODO: implement rounding strategy: ceiling, flooring
    public static double round(double value, int floatingPoint) {
        assertNonNegativeFloatingPoint(floatingPoint);

        final double scale = Math.pow(10, floatingPoint);
        return (double) Math.round(value * scale) / scale;
    }

    public static double toPercentage(double value, int roundToFloatingPoint) {
        assertNonNegativeFloatingPoint(roundToFloatingPoint + 2);

        final double scale = Math.pow(10, roundToFloatingPoint + 2);
        return (double) Math.round(value * scale) * 100 / scale;
    }

    private static void assertNonNegativeFloatingPoint(int floatingPoint) {
        if (floatingPoint < 0) {
            throw new RuntimeException("Floating point should be equal or greater than 0");
        }
    }

    /**
     * Adds support for Period objects which is missing for instant
     * <p>
     * TODO: optimize for Duration, just check it before and call it wo this conversion
     *
     * @param startDate
     * @param window
     * @return
     */
    public static Instant instantPlusTemporalAmount(Instant startDate, TemporalAmount window) {
        return ZonedDateTime.ofInstant(startDate, ZoneOffset.UTC).plus(window).toInstant();
    }

    public static DoubleStream streamRecentNumbers(Collection<Double> prices, int period, int recentNumberIndex) {
        return prices.stream()
                     .skip(recentNumberIndex + 1 - period)
                     .limit(period)
                     .mapToDouble(Double::doubleValue);
    }
}
