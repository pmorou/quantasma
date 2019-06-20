package quantasma.app.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import quantasma.core.analysis.parametrize.Parameterizable;

import java.time.LocalDate;
import java.time.Period;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public class BacktestRequest {
    private final String title;
    private final Time time;
    private final List<Parameter> parameters;
    private final List<Criterion> criterions;

    @JsonCreator
    public BacktestRequest(@JsonProperty("title") String title,
                           @JsonProperty("time") Time time,
                           @JsonProperty("parameters") List<Parameter> parameters,
                           @JsonProperty("criterions") List<Criterion> criterions) {
        this.title = title;
        this.time = time;
        this.parameters = parameters;
        this.criterions = criterions;
    }

    public Map<String, Object[]> parameters(Parameterizable[] referenceParameters) {
        if (parameters.size() != referenceParameters.length) {
            throw new IllegalArgumentException(String.format("Parameters number doesn't match - provided [%s], requiring [%s]",
                parameters.size(), referenceParameters.length));
        }

        final Map<String, String> mappedInputs = parameters.stream()
            .collect(Collectors.toMap(
                Parameter::getName, Parameter::getValue));

        return Arrays.stream(referenceParameters)
            .collect(Collectors.toMap(
                getVerifiedName(mappedInputs),
                getTypedValues(mappedInputs)));
    }

    private Function<Parameterizable, Object[]> getTypedValues(Map<String, String> mappedInputs) {
        return parameter -> {
            final String values = mappedInputs.get(parameter.name());
            return Arrays.stream(values.split(","))
                .map(String::trim)
                .map(value -> {
                    if (parameter.javaType() == String.class) {
                        return value;
                    } else if (parameter.javaType() == Integer.class) {
                        return Integer.parseInt(value);
                    } else {
                        throw new IllegalArgumentException(String.format("Unsupported type for parameter [%s]", value));
                    }
                })
                .toArray();
        };
    }

    private static Function<Parameterizable, String> getVerifiedName(Map<String, String> mappedInputs) {
        return parameter -> {
            if (!mappedInputs.containsKey(parameter.name())) {
                throw new IllegalArgumentException(String.format("%s parameter not provided", parameter));
            }
            return parameter.name();
        };
    }

    public List<String> criterionNames() {
        return getCriterions()
            .stream()
            .map(Criterion::getName)
            .collect(Collectors.toList());
    }

    @Getter
    public static class Time {
        private final LocalDate from;
        private final String window;

        @JsonCreator
        public Time(@JsonProperty("from") LocalDate from,
                    @JsonProperty("window") String window) {
            this.from = from;
            this.window = window;
        }

        public Period getWindowAsPeriod() {
            return Period.parse(window);
        }
    }

    @Getter
    public static class Parameter {
        private final String name;
        private final String value;

        @JsonCreator
        public Parameter(@JsonProperty("name") String name,
                         @JsonProperty("value") String value) {
            this.name = name;
            this.value = value;
        }
    }

    @Getter
    public static class Criterion {
        private final String name;

        @JsonCreator
        public Criterion(@JsonProperty("name") String name) {
            this.name = name;
        }
    }
}
