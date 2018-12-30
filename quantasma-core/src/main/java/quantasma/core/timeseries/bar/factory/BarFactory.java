package quantasma.core.timeseries.bar.factory;

import org.ta4j.core.Bar;
import org.ta4j.core.num.Num;
import quantasma.core.BarPeriod;

import java.util.function.Function;

public interface BarFactory<B extends Bar> {

    B create(BarPeriod barPeriod, Function<Number, Num> numFunction);

}
