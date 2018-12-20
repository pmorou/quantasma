package quantasma.app.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import quantasma.core.BarPeriod;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

@Getter
public class FeedHistoricalBarsRequest {
    private final String symbol;
    private final BarPeriod barPeriod;
    private final LocalDate fromDate;
    private final LocalDate toDate;

    @JsonCreator
    public FeedHistoricalBarsRequest(@JsonProperty("symbol") String symbol,
                                     @JsonProperty("barPeriod") BarPeriod barPeriod,
                                     @JsonProperty("fromDate") LocalDate fromDate,
                                     @JsonProperty("toDate") LocalDate toDate) {
        this.symbol = symbol;
        this.barPeriod = barPeriod;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    public Instant fromDateAsUtc() {
        return fromDate.atStartOfDay().toInstant(ZoneOffset.UTC);
    }

    public Instant toDateAsUtc() {
        return toDate.atStartOfDay().toInstant(ZoneOffset.UTC);
    }

}
