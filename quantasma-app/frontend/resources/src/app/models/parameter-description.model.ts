export interface ParameterDescription {
  name?: string;
  type?: string;
  parameterDefinition?: ParameterDefinition;
}

export type ParameterType = 'NUMBER' | "TEXT" | "DICTIONARY";

export interface ParameterDefinition {
  parameterType?: ParameterType;
  min?: number;
  max?: number;
  pattern?: string;
  values?: any[];
}