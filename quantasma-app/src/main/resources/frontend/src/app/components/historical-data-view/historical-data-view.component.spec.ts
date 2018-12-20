import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { HistoricalDataViewComponent } from './historical-data-view.component';

describe('HistoricalDataViewComponent', () => {
  let component: HistoricalDataViewComponent;
  let fixture: ComponentFixture<HistoricalDataViewComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ HistoricalDataViewComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HistoricalDataViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
