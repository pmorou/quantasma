package quantasma.app.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import quantasma.core.BarPeriod;

import java.time.Instant;

@Getter
public class FeedSymbolTicksRequest {
    private final String symbol;
    private final BarPeriod barPeriod;
    private final Instant fromDate;
    private final Instant toDate;

    @JsonCreator
    public FeedSymbolTicksRequest(@JsonProperty("symbol") String symbol,
                                  @JsonProperty("barPeriod") BarPeriod barPeriod,
                                  @JsonProperty("fromDate") Instant fromDate,
                                  @JsonProperty("toDate") Instant toDate) {
        this.symbol = symbol;
        this.barPeriod = barPeriod;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }
}
