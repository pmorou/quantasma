export class AccountState {
  constructor(public equity: number,
              public balance: number,
              public positionsProfitLoss: number,
              public positionsAmount: number,
              public usedMargin: number,
              public currency: string,
              public leverage: number) {
    this.equity = equity;
    this.balance = balance;
    this.positionsProfitLoss = positionsProfitLoss;
    this.positionsAmount = positionsAmount;
    this.usedMargin = usedMargin;
    this.currency = currency;
    this.leverage = leverage;
  }
}