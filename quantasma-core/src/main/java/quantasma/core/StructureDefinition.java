package quantasma.core;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import quantasma.core.timeseries.TimeSeriesDefinition;
import quantasma.core.timeseries.bar.BarFactory;
import quantasma.core.timeseries.bar.OneSidedBar;

import java.util.Objects;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class StructureDefinition<B extends OneSidedBar> {
    private final Model<B> model;
    private final Resolution resolution;

    public static <B extends OneSidedBar> Model<B> model(BarFactory<B> barFactory) {
        return new Model<>(Objects.requireNonNull(barFactory));
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Getter
    public static class Model<B extends OneSidedBar> {
        private final BarFactory<B> barFactory;

        public StructureDefinition<B> resolution(TimeSeriesDefinition timeSeriesDefinition) {
            return new StructureDefinition<>(new Model<>(barFactory),
                                             new Resolution(Objects.requireNonNull(timeSeriesDefinition)));
        }
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Getter
    public static class Resolution {
        private final TimeSeriesDefinition timeSeriesDefinition;
    }
}
