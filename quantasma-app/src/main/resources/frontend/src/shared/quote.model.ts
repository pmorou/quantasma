export class Quote {
  constructor(public symbol: string,
              public time: Date,
              public bid: number,
              public ask: number) {
    this.symbol = symbol;
    this.time = time;
    this.bid = bid;
    this.ask = ask;
  }
}