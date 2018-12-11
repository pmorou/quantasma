package quantasma.core.analysis.parametrize;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Data
public class Parameters {
    Map<String, Object> parameters = new HashMap<>();

    public Parameters add(String key, Object value) {
        parameters.put(key, value);
        return this;
    }

    public Parameters addAll(Parameters parameters) {
        this.parameters.putAll(parameters.parameters);
        return this;
    }

    public Set<String> keys() {
        return parameters.keySet();
    }

    public Object get(String key) {
        return parameters.get(key);
    }

}
