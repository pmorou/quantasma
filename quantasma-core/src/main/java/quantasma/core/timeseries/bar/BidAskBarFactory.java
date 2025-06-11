package quantasma.core.timeseries.bar;

import org.ta4j.core.num.NumFactory;
import quantasma.core.BarPeriod;

import java.time.Instant;

public class BidAskBarFactory implements BarFactory<BidAskBar> {

    @Override
    public BidAskBar create(BarPeriod barPeriod, NumFactory numFactory) {
        return create(barPeriod, numFactory, Instant.now());
    }

    @Override
    public BidAskBar create(BarPeriod barPeriod, NumFactory numFactory, Instant time) {
        return createInstance(barPeriod, numFactory, time);
    }

    private static BaseBidAskBar createInstance(BarPeriod barPeriod, NumFactory numFactory, Instant time) {
        return new BaseBidAskBar(barPeriod.getPeriod(),
            time,
            numFactory);
    }

    @Override
    public BidAskBar getNaNBar() {
        return BaseBidAskBar.NaNBar.getInstance();
    }

}
