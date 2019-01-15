package quantasma.core.analysis.parametrize;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Values<P extends Enum & Parameterizable> {
    private final Class<P> parameterClazz;

    @Getter
    private final Map<P, Object> valuesByParameter = new HashMap<>();

    private Values(Class<P> parameterClazz) {
        this.parameterClazz = parameterClazz;
    }

    public static <P extends Enum & Parameterizable> Values<P> of(Class<P> parameterClazz) {
        return new Values<>(parameterClazz);
    }

    public Values<P> set(P key, Object value) {
        valuesByParameter.put(key, value);
        return this;
    }

    public Values<P> set(String key, Object value) {
        set(getEnum(key), value);
        return this;
    }

    private P getEnum(String key) {
        return (P) Enum.valueOf(parameterClazz, key);
    }

    public Values<P> setAll(Values<P> values) {
        this.valuesByParameter.putAll(values.getValuesByParameter());
        return this;
    }

    public Set<P> keys() {
        return valuesByParameter.keySet();
    }

    public Object get(P parameter) {
        return valuesByParameter.get(parameter);
    }

    public Integer getInteger(P parameter) {
        return (Integer) valuesByParameter.get(parameter);
    }

    public String getString(P parameter) {
        return (String) valuesByParameter.get(parameter);
    }

    public Object get(String key) {
        return valuesByParameter.get(getEnum(key));
    }

}
