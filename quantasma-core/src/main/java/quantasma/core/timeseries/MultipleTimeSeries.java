package quantasma.core.timeseries;

import quantasma.core.BarPeriod;
import quantasma.core.Quote;
import quantasma.core.timeseries.bar.OneSidedBar;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

public interface MultipleTimeSeries<B extends OneSidedBar> extends Serializable {
    MultipleTimeSeries<B> aggregate(TimeSeriesDefinition timeSeriesDefinition);

    void updateBar(Quote quote);

    void createBar(Instant priceDate);

    int lastBarIndex();

    String getSymbol();

    GenericTimeSeries<B> getTimeSeries(BarPeriod period);

    List<GenericTimeSeries<B>> getTimeSeries();

    MainTimeSeries<B> getMainTimeSeries();
}
