import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BacktestDashboardComponent } from './backtest-dashboard.component';

describe('BacktestComponent', () => {
  let component: BacktestDashboardComponent;
  let fixture: ComponentFixture<BacktestDashboardComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BacktestDashboardComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BacktestDashboardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
