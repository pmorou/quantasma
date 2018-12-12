package quantasma.core;

import lombok.Data;

import java.util.List;

@Data
public class StrategyDescription {
    private final Long id;
    private final String name;
    private final boolean active;
    private final List<Parameter> parameters;

    @Data
    public static class Parameter {
        private final String name;
        private final String clazz;
        private final Object value;
    }
}
