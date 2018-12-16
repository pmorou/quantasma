import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from "@angular/router";
import { BacktestService } from "../backtest.service";
import { Backtest, Parameter } from "../../shared/backtest.model";

@Component({
  selector: 'app-backtest-strategy',
  templateUrl: './backtest-strategy.component.html',
  styleUrls: ['./backtest-strategy.component.scss']
})
export class BacktestStrategyComponent implements OnInit {

  public backtestName$: string = "";
  public backtest?: Backtest;

  availableParameters: Parameter[] = [];
  availableCriterions: string[] = [];

  result: any;

  constructor(private route: ActivatedRoute, private backtestService: BacktestService) {
    this.route.params.subscribe(params =>
      this.backtestName$ = params.name
    );
    this.backtestService.criterions().subscribe(value => {
      this.availableCriterions = <string[]>value;
    })
  }

  ngOnInit() {
    this.backtestService.get(this.backtestName$).subscribe(value => {
        this.backtest = <Backtest> value;
        this.availableParameters = this.backtest.parameters;
      }
    );
  }

  testFinished($event: any) {
    console.log({$event});
    this.result = $event;
  }

  runTest() {
  }
}
