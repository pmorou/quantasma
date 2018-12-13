import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BacktestComponent } from './backtest.component';

describe('BacktestComponent', () => {
  let component: BacktestComponent;
  let fixture: ComponentFixture<BacktestComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BacktestComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BacktestComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
