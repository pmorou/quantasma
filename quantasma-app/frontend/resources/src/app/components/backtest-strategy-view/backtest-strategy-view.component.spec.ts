import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BacktestStrategyViewComponent } from './backtest-strategy-view.component';

describe('BacktestStrategyViewComponent', () => {
  let component: BacktestStrategyViewComponent;
  let fixture: ComponentFixture<BacktestStrategyViewComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BacktestStrategyViewComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BacktestStrategyViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
