import { TestBed } from '@angular/core/testing';

import { BacktestService } from './backtest.service';

describe('BacktestService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: BacktestService = TestBed.get(BacktestService);
    expect(service).toBeTruthy();
  });
});
