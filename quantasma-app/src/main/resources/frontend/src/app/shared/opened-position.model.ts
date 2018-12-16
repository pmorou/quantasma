export class OpenedPosition {
  constructor(public symbol: string,
              public direction: Direction,
              public amount: number,
              public price: number,
              public stopLoss: number,
              public takeProfit: number,
              public profitLossPips: number,
              public profitLoss: number) {
    this.symbol = symbol;
    this.direction = direction;
    this.amount = amount;
    this.price = price;
    this.stopLoss = stopLoss;
    this.takeProfit = takeProfit;
    this.profitLossPips = profitLossPips;
    this.profitLoss = profitLoss;
  }
}

enum Direction {
  LONG,
  SHORT
}