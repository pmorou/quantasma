package quantasma.core.indicators;

import org.ta4j.core.Indicator;
import org.ta4j.core.indicators.CachedIndicator;
import org.ta4j.core.num.Num;
import quantasma.core.timeseries.GenericTimeSeries;

public class SpreadIndicator extends CachedIndicator<Num> {

  private final Indicator<Num> ask;
  private final Indicator<Num> bid;

  public SpreadIndicator(GenericTimeSeries timeSeries, Indicator<Num> askIndicator, Indicator<Num> bidIndicator) {
    super(timeSeries.plainTimeSeries());
    this.ask = askIndicator;
    this.bid = bidIndicator;
  }

  @Override
  protected Num calculate(int index) {
    return ask.getValue(index).minus(bid.getValue(index));
  }

  @Override
  public int getCountOfUnstableBars() {
    return 0;
  }
}
