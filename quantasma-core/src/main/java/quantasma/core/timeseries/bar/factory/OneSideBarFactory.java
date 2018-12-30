package quantasma.core.timeseries.bar.factory;

import org.ta4j.core.BaseBar;
import org.ta4j.core.num.Num;
import quantasma.core.BarPeriod;
import quantasma.core.DateUtils;
import quantasma.core.timeseries.bar.BaseOneSideBar;
import quantasma.core.timeseries.bar.OneSideBar;

import java.time.ZonedDateTime;
import java.util.function.Function;

public class OneSideBarFactory implements BarFactory<OneSideBar> {

    @Override
    public OneSideBar create(BarPeriod barPeriod, Function<Number, Num> numFunction) {
        return new BaseOneSideBar(new BaseBar(barPeriod.getPeriod(),
                                              DateUtils.createEndDate(ZonedDateTime.now(), barPeriod),
                                              numFunction));
    }

    @Override
    public OneSideBar create(BarPeriod barPeriod, ZonedDateTime time, Function<Number, Num> numFunction) {
        return new BaseOneSideBar(new BaseBar(barPeriod.getPeriod(),
                                              DateUtils.createEndDate(time, barPeriod),
                                              numFunction));
    }
}
