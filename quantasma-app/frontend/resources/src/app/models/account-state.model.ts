export interface AccountState {
  equity: number;
  balance: number;
  positionsProfitLoss: number;
  positionsAmount: number;
  usedMargin: number;
  currency: string;
  leverage: number;
}