package quantasma.core.timeseries.bar.factory;

import org.ta4j.core.num.Num;
import quantasma.core.BarPeriod;
import quantasma.core.DateUtils;
import quantasma.core.timeseries.bar.BaseBidAskBar;
import quantasma.core.timeseries.bar.BidAskBar;

import java.time.ZonedDateTime;
import java.util.function.Function;

public class BidAskBarFactory implements BarFactory<BidAskBar> {

    @Override
    public BidAskBar create(BarPeriod barPeriod, Function<Number, Num> numFunction) {
        return new BaseBidAskBar(barPeriod.getPeriod(),
                                 DateUtils.createEndDate(ZonedDateTime.now(), barPeriod),
                                 numFunction);
    }

}
