import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from "@angular/router";
import { BacktestService } from "../backtest.service";
import { Backtest } from "../../shared/Backtest";

@Component({
  selector: 'app-backtest-strategy',
  templateUrl: './backtest-strategy.component.html',
  styleUrls: ['./backtest-strategy.component.scss']
})
export class BacktestStrategyComponent implements OnInit {

  private backtestName$: string = "";

  public backtest?: Backtest;

  constructor(private route: ActivatedRoute, private backtestService: BacktestService) {
    this.route.params.subscribe(params =>
      this.backtestName$ = params.name
    )
  }

  ngOnInit() {
    this.backtestService.get(this.backtestName$).subscribe(value =>
      this.backtest = <Backtest> value
    );
  }

  runTest() {
  }
}
