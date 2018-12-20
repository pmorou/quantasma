import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BacktestStrategySettingsComponent } from './backtest-strategy-settings.component';

describe('BacktestStrategySettingsComponent', () => {
  let component: BacktestStrategySettingsComponent;
  let fixture: ComponentFixture<BacktestStrategySettingsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BacktestStrategySettingsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BacktestStrategySettingsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
