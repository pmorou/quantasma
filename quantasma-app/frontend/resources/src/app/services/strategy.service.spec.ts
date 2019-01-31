import { TestBed } from '@angular/core/testing';

import { StrategyService } from './strategy.service';

describe('StrategyService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: StrategyService = TestBed.get(StrategyService);
    expect(service).toBeTruthy();
  });
});
