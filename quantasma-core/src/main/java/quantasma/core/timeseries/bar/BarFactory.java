package quantasma.core.timeseries.bar;

import org.ta4j.core.num.Num;
import quantasma.core.BarPeriod;

import java.time.ZonedDateTime;
import java.util.function.Function;

public interface BarFactory<B extends OneSidedBar> {

    B create(BarPeriod barPeriod, Function<Number, Num> numFunction);

    B create(BarPeriod barPeriod, Function<Number, Num> numFunction, ZonedDateTime time);

    B getNaNBar();

}
