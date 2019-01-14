package quantasma.core.timeseries.bar;

import org.ta4j.core.num.Num;
import quantasma.core.BarPeriod;
import quantasma.core.DateUtils;

import java.time.ZonedDateTime;
import java.util.function.Function;

public class BidAskBarFactory implements BarFactory<BidAskBar> {

    @Override
    public BidAskBar create(BarPeriod barPeriod, Function<Number, Num> numFunction) {
        return create(barPeriod, numFunction, ZonedDateTime.now());
    }

    @Override
    public BidAskBar create(BarPeriod barPeriod, Function<Number, Num> numFunction, ZonedDateTime time) {
        return createInstance(barPeriod, numFunction, time);
    }

    private static BaseBidAskBar createInstance(BarPeriod barPeriod, Function<Number, Num> numFunction, ZonedDateTime time) {
        return new BaseBidAskBar(barPeriod.getPeriod(),
                                 DateUtils.createEndDate(time, barPeriod),
                                 numFunction);
    }

    @Override
    public BidAskBar getNaNBar() {
        return BaseBidAskBar.NaNBar.getInstance();
    }

}
