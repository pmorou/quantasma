import { ParameterDescription } from "./parameter-description.model";

export interface Backtest {
  name: string;
  strategy: string;
  parameters: ParameterDescription[];
}
