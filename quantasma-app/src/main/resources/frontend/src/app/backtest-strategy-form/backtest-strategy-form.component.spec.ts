import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BacktestStrategyFormComponent } from './backtest-strategy-form.component';

describe('ProductFormComponent', () => {
  let component: BacktestStrategyFormComponent;
  let fixture: ComponentFixture<BacktestStrategyFormComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BacktestStrategyFormComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BacktestStrategyFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
