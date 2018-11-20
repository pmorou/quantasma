package quantasma.core;

import quantasma.core.timeseries.MultipleTimeSeries;

import java.time.ZonedDateTime;

public interface DataService {
    MultipleTimeSeries getMultipleTimeSeries(String symbol);

    void add(String symbol, ZonedDateTime date, double price);

    void add(String symbol, ZonedDateTime date, double bid, double ask);

    int lastBarIndex();
}
