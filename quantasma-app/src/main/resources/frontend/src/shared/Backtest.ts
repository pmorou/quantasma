export class Backtest {
  constructor(public name: string,
              public strategy: string,
              public parameters: Parameter[]) {
    this.name = name;
    this.strategy = strategy;
    this.parameters = parameters;
  }
}

class Parameter {
  constructor(public name: string,
              public type: string) {
    this.name = name;
    this.type = type;
  }
}