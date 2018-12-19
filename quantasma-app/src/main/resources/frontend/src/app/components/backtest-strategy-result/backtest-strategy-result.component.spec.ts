import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BacktestStrategyResultComponent } from './backtest-strategy-result.component';

describe('BacktestStrategyResultComponent', () => {
  let component: BacktestStrategyResultComponent;
  let fixture: ComponentFixture<BacktestStrategyResultComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BacktestStrategyResultComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BacktestStrategyResultComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
