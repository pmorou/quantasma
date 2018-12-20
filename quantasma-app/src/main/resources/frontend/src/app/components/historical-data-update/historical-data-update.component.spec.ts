import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { HistoricalDataUpdateComponent } from './historical-data-update.component';

describe('HistoricalDataUpdateComponent', () => {
  let component: HistoricalDataUpdateComponent;
  let fixture: ComponentFixture<HistoricalDataUpdateComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ HistoricalDataUpdateComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HistoricalDataUpdateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
