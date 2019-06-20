package quantasma.core.analysis.parametrize;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

public interface Parameterizable {

    String name();

    Class<?> javaType();

    ParameterDefinition parameterDefinition();

    interface ParameterDefinition {
        ParameterType getParameterType();
    }

    enum ParameterType {
        NUMBER,
        TEXT,
        DICTIONARY
    }

    @Getter
    @Builder
    class Number implements ParameterDefinition {
        private final ParameterType parameterType = ParameterType.NUMBER;

        /**
         * Min available number
         */
        private final Integer min;

        /**
         * Max available number
         */
        private final Integer max;
    }

    @Getter
    @Builder
    class Text implements ParameterDefinition {
        private final ParameterType parameterType = ParameterType.TEXT;

        /**
         * Pattern describing allowed value
         */
        private final String pattern;
    }

    @Getter
    @Builder
    class Dictionary implements ParameterDefinition {
        private final ParameterType parameterType = ParameterType.DICTIONARY;

        /**
         * Source name resolvable to the available values
         */
        private String source;

        /**
         * Resolved values
         */
        private List<Object> values;
    }
}