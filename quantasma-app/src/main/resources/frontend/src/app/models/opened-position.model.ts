export interface OpenedPosition {
  symbol: string;
  direction: Direction;
  amount: number
  price: number;
  stopLoss: number;
  takeProfit: number;
  profitLossPips: number;
  profitLoss: number;
}

enum Direction {
  LONG,
  SHORT
}