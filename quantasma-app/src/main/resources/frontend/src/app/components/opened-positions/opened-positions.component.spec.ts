import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { OpenedPositionsComponent } from './opened-positions.component';

describe('OpenedPositionsComponent', () => {
  let component: OpenedPositionsComponent;
  let fixture: ComponentFixture<OpenedPositionsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ OpenedPositionsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(OpenedPositionsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
