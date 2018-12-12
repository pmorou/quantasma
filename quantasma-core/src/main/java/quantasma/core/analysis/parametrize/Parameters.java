package quantasma.core.analysis.parametrize;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Parameters<T extends Enum & Parameter> {
    private final Class<T> clazz;

    @Getter
    Map<T, Object> parameters = new HashMap<>();

    public Parameters(Class<T> clazz) {
        this.clazz = clazz;
    }

    public static <T extends Enum & Parameter> Parameters<T> instance(Class<T> clazz) {
        return new Parameters<>(clazz);
    }

    public Parameters<T> add(T key, Object value) {
        parameters.put(key, value);
        return this;
    }

    public Parameters<T> add(String key, Object value) {
        add(getEnum(key), value);
        return this;
    }

    private T getEnum(String key) {
        return (T) Enum.valueOf(clazz, key);
    }

    public Parameters<T> addAll(Parameters<T> parameters) {
        this.parameters.putAll(parameters.getParameters());
        return this;
    }

    public Set<T> keys() {
        return parameters.keySet();
    }

    public Object get(T parameter) {
        return parameters.get(parameter);
    }

    public Object get(String key) {
        return parameters.get(getEnum(key));
    }

}
