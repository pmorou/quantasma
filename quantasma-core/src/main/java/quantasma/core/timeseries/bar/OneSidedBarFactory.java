package quantasma.core.timeseries.bar;

import org.ta4j.core.num.NumFactory;
import quantasma.core.BarPeriod;

import java.time.Instant;

public class OneSidedBarFactory implements BarFactory<OneSidedBar> {

    @Override
    public OneSidedBar create(BarPeriod barPeriod, NumFactory numFactory) {
        return create(barPeriod, numFactory, Instant.now());
    }

    @Override
    public OneSidedBar create(BarPeriod barPeriod, NumFactory numFactory, Instant time) {
        return createInstance(barPeriod, numFactory, time);
    }

    private static BaseOneSidedBar createInstance(BarPeriod barPeriod, NumFactory numFactory, Instant now) {
        return new BaseOneSidedBar(barPeriod.getPeriod(),
                                   now,
                                   numFactory);
    }

    @Override
    public OneSidedBar getNaNBar() {
        return BaseOneSidedBar.NaNBar.getInstance();
    }

}
