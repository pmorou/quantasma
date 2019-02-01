import { TestBed } from '@angular/core/testing';

import { HistoricalDataService } from './historical-data.service';

describe('HistoricalDataService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: HistoricalDataService = TestBed.get(HistoricalDataService);
    expect(service).toBeTruthy();
  });
});
