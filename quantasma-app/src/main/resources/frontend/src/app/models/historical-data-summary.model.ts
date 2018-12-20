export interface HistoricalDataSummary {
  [key: string]: Data[];
}

export class HistoricalDataSummaryHolder {
  constructor(private data: HistoricalDataSummary) {
    this.data = data;
  }

  ofSymbol(symbol: string): Data[] {
    return this.data[symbol];
  }

  symbols(): string[] {
    return Object.keys(this.data);
  }
}

interface Data {
  symbol: string;
  period: string;
  fromDate: Date;
  toDate: Date;
  barCount: number;
}
