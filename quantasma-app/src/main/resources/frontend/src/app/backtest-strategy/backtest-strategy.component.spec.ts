import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BacktestStrategyComponent } from './backtest-strategy.component';

describe('BacktestStrategyComponent', () => {
  let component: BacktestStrategyComponent;
  let fixture: ComponentFixture<BacktestStrategyComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BacktestStrategyComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BacktestStrategyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
