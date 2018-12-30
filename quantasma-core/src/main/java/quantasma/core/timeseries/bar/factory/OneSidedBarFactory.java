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
        return new BaseOneSidedBar(new BaseBar(barPeriod.getPeriod(),
                                               DateUtils.createEndDate(ZonedDateTime.now(), barPeriod),
                                               numFunction));
    }

    @Override
    public OneSidedBar create(BarPeriod barPeriod, ZonedDateTime time, Function<Number, Num> numFunction) {
        return new BaseOneSidedBar(new BaseBar(barPeriod.getPeriod(),
                                               DateUtils.createEndDate(time, barPeriod),
                                               numFunction));
    }
}
