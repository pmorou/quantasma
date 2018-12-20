import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { HistoricalDataFeedComponent } from './historical-data-feed.component';

describe('HistoricalDataFeedComponent', () => {
  let component: HistoricalDataFeedComponent;
  let fixture: ComponentFixture<HistoricalDataFeedComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ HistoricalDataFeedComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HistoricalDataFeedComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
