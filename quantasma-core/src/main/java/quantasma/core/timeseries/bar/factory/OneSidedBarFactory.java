package quantasma.core.timeseries.bar.factory;

import org.ta4j.core.BaseBar;
import org.ta4j.core.num.Num;
import quantasma.core.BarPeriod;
import quantasma.core.DateUtils;
import quantasma.core.timeseries.bar.BaseOneSidedBar;
import quantasma.core.timeseries.bar.OneSidedBar;

import java.time.ZonedDateTime;
import java.util.function.Function;

public class OneSidedBarFactory implements BarFactory<OneSidedBar> {

    @Override
    public OneSidedBar create(BarPeriod barPeriod, Function<Number, Num> numFunction) {
        return create(barPeriod, numFunction, ZonedDateTime.now());
    }

    @Override
    public OneSidedBar create(BarPeriod barPeriod, Function<Number, Num> numFunction, ZonedDateTime time) {
        return createInstance(barPeriod, numFunction, time);
    }

    private static BaseOneSidedBar createInstance(BarPeriod barPeriod, Function<Number, Num> numFunction, ZonedDateTime now) {
        return new BaseOneSidedBar(new BaseBar(barPeriod.getPeriod(),
                                               DateUtils.createEndDate(now, barPeriod),
                                               numFunction));
    }

}
