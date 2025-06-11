package quantasma.core.timeseries.bar;

import org.ta4j.core.num.NumFactory;
import quantasma.core.BarPeriod;

import java.time.Instant;
import java.time.ZonedDateTime;

public interface BarFactory<B extends OneSidedBar> {

    B create(BarPeriod barPeriod, NumFactory numFactory);

    B create(BarPeriod barPeriod, NumFactory numFactory, Instant time);

    B getNaNBar();

}
