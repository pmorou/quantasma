import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from "@angular/router";
import { BacktestService } from "../backtest.service";
import { Backtest } from "../../shared/backtest.model";

@Component({
  selector: 'app-backtest-strategy',
  templateUrl: './backtest-strategy.component.html',
  styleUrls: ['./backtest-strategy.component.scss']
})
export class BacktestStrategyComponent implements OnInit {

  public backtestName$: string = "";
  public backtest?: Backtest;

  availableParameters: Object[] = [{name:'PARAM1', type:'integer'}, {name:'PARAM2', type:'integer'}, {name:'PARAM3', type:'integer'}];
  availableCriterions: string[] = ['criterion1', 'criterion2', 'criterion3', 'criterion4'];

  constructor(private route: ActivatedRoute, private backtestService: BacktestService) {
    this.route.params.subscribe(params =>
      this.backtestName$ = params.name
    );
  }

  ngOnInit() {
    this.backtestService.get(this.backtestName$).subscribe(value => {
        this.backtest = <Backtest> value;
      }
    );
  }

  testFinished($event: any) {
    console.log({$event});
  }

  runTest() {
  }
}
