package quantasma.core

import quantasma.core.timeseries.UniversalTimeSeries
import quantasma.core.timeseries.bar.OneSidedBar

import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.ZonedDateTime

abstract class Utils {

    static OneSidedBar nanBar(UniversalTimeSeries timeSeries) {
        return timeSeries.getBarFactory().getNaNBar()
    }

    static ZonedDateTime utc(LocalDateTime localDateTime) {
        return ZonedDateTime.of(localDateTime, ZoneOffset.UTC)
    }

    private Utils() {
        // static helper class
    }

}
