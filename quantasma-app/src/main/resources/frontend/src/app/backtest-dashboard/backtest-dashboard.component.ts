import { Component, OnInit } from '@angular/core';
import { BacktestService } from "../backtest.service";

@Component({
  selector: 'app-backtest-dashboard',
  templateUrl: './backtest-dashboard.component.html',
  styleUrls: ['./backtest-dashboard.component.scss']
})
export class BacktestDashboardComponent implements OnInit {

  backtests$: any = [];

  constructor(private backtestService: BacktestService) { }

  ngOnInit() {
    this.backtestService.all().subscribe(value => {
      this.backtests$ = value
    });
  }

}
