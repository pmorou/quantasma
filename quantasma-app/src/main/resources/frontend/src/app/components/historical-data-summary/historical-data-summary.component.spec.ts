import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { HistoricalDataSummaryComponent } from './historical-data-summary.component';

describe('HistoricalDataSummaryComponent', () => {
  let component: HistoricalDataSummaryComponent;
  let fixture: ComponentFixture<HistoricalDataSummaryComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ HistoricalDataSummaryComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HistoricalDataSummaryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
