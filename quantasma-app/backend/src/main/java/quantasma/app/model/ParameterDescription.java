package quantasma.app.model;

import lombok.Data;
import quantasma.core.analysis.parametrize.Parameterizable.ParameterDefinition;

@Data
public class ParameterDescription {
    private final String name;
    private final String type;
    private final ParameterDefinition parameterDefinition;
}