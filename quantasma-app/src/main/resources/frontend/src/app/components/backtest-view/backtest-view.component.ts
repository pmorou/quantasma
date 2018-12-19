import { Component, OnInit } from '@angular/core';
import { BacktestService } from "../../services/backtest.service";
import { Backtest } from "../../models/backtest.model";

@Component({
  selector: 'app-backtest-view',
  templateUrl: './backtest-view.component.html',
  styleUrls: ['./backtest-view.component.scss']
})
export class BacktestViewComponent implements OnInit {
  backtests$: Backtest[] = [];

  constructor(private backtestService: BacktestService) { }

  ngOnInit() {
    this.backtestService.all().subscribe(value => {
      this.backtests$ = value
    });
  }

}
