import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BacktestViewComponent } from './backtest-dashboard.component';

describe('BacktestComponent', () => {
  let component: BacktestViewComponent;
  let fixture: ComponentFixture<BacktestViewComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BacktestViewComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BacktestViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
