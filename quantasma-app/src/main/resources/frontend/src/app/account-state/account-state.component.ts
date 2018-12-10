import { Component, OnInit } from '@angular/core';
import {EventsService} from "../events.service";

@Component({
  selector: 'app-account-state',
  templateUrl: './account-state.component.html',
  styleUrls: ['./account-state.component.scss']
})
export class AccountStateComponent implements OnInit {

  public equity: number = NaN;
  public balance: number = NaN;
  public positionsProfitLoss: number = NaN;
  public positionsAmount: number = NaN;
  public usedMargin: number = NaN;
  public currency: string = "";
  public leverage: number = NaN;

  constructor(private events: EventsService) { }

  ngOnInit() {
    this.events.accountState().subscribe(value => {
      this.equity = value.equity;
      this.balance = value.balance;
      this.positionsProfitLoss = value.positionsProfitLoss;
      this.positionsAmount = value.positionsAmount;
      this.usedMargin = value.usedMargin;
      this.currency = value.currency;
      this.leverage = value.leverage;
    });
  }

}
