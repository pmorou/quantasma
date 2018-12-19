import { ParameterValue } from "./parameter-value.model";

export interface Strategy {
  id: number;
  name: string;
  parameters: ParameterValue[];
  active: boolean;
}