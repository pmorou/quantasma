import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AccountStateComponent } from './account-state.component';

describe('AccountStateComponent', () => {
  let component: AccountStateComponent;
  let fixture: ComponentFixture<AccountStateComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AccountStateComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AccountStateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
