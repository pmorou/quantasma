package quantasma.app.model;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import quantasma.core.BarPeriod;

import java.io.Serializable;
import java.time.Instant;

@Data
public class PersistentOhlcvTick implements Serializable {

    @Id
    private ObjectId id;
    @Indexed
    private BarPeriod period;
    @Indexed(unique = true)
    private Instant date;
    @Indexed
    private String symbol;
    private double bidOpen;
    private double bidLow;
    private double bidHigh;
    private double bidClose;
    private double askOpen;
    private double askLow;
    private double askHigh;
    private double askClose;
    private int volume;

    public PersistentOhlcvTick() {
    }

    public PersistentOhlcvTick(BarPeriod period, Instant date, String symbol, double bidOpen, double bidLow, double bidHigh, double bidClose, double askOpen, double askLow, double askHigh, double askClose, int volume) {
        this.period = period;
        this.date = date;
        this.symbol = symbol;
        this.bidOpen = bidOpen;
        this.bidLow = bidLow;
        this.bidHigh = bidHigh;
        this.bidClose = bidClose;
        this.askOpen = askOpen;
        this.askLow = askLow;
        this.askHigh = askHigh;
        this.askClose = askClose;
        this.volume = volume;
    }

    public static PersistentOhlcvTick from(OhlcvTick ohlcvTick) {
        return new PersistentOhlcvTick(ohlcvTick.getPeriod(),
                                       ohlcvTick.getDate(),
                                       ohlcvTick.getSymbol(),
                                       ohlcvTick.getBidOpen(),
                                       ohlcvTick.getBidLow(),
                                       ohlcvTick.getBidHigh(),
                                       ohlcvTick.getBidClose(),
                                       ohlcvTick.getAskOpen(),
                                       ohlcvTick.getAskLow(),
                                       ohlcvTick.getAskHigh(),
                                       ohlcvTick.getAskClose(),
                                       ohlcvTick.getVolume());
    }
}
