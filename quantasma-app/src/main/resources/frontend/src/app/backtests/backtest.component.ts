import { Component, OnInit } from '@angular/core';
import { BacktestService } from "../backtest.service";

@Component({
  selector: 'app-backtest',
  templateUrl: './backtest.component.html',
  styleUrls: ['./backtest.component.scss']
})
export class BacktestComponent implements OnInit {

  backtests$: any = [];

  constructor(private backtestService: BacktestService) { }

  ngOnInit() {
    this.backtestService.all().subscribe(value => {
      this.backtests$ = value
    });
  }

}
