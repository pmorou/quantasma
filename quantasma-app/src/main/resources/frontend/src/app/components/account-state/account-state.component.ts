import { Component, OnInit } from '@angular/core';
import { EventsService } from "../../events.service";
import { AccountState } from "../../shared/account-state.model";

@Component({
  selector: 'app-account-state',
  templateUrl: './account-state.component.html',
  styleUrls: ['./account-state.component.scss']
})
export class AccountStateComponent implements OnInit {
  accountState$?: AccountState;

  constructor(private events: EventsService) { }

  ngOnInit() {
    this.events.accountState().subscribe(value => {
      this.accountState$ = value;
    });
  }

}
