import {Component, OnInit, ViewChild} from '@angular/core';
import { ActivatedRoute } from "@angular/router";
import { BacktestService } from "../backtest.service";
import { Backtest, Parameter } from "../shared/backtest.model";
import {BacktestStrategyResultComponent} from "../backtest-strategy-result/backtest-strategy-result.component";

@Component({
  selector: 'app-backtest-strategy',
  templateUrl: './backtest-strategy.component.html',
  styleUrls: ['./backtest-strategy.component.scss']
})
export class BacktestStrategyComponent implements OnInit {
  backtestName$: string = "";
  backtest?: Backtest;

  availableParameters: Parameter[] = [];
  availableCriterions: string[] = [];

  @ViewChild("backtestResult")
  backtestResult?: BacktestStrategyResultComponent;

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
    const testResult = this.flatten($event);
    if (this.backtestResult != undefined) {
      this.backtestResult.updateTable(testResult);
    }
  }

  /**
   * Given an array:
   * [ { obj1: { key1: 'val1' } , obj2: { key2: 'val2' } } ]
   * Calling this method will result in:
   * [ { key1: "val1", key2: "val2" } ]
   *
   * @param {any[]} json
   * @returns {any[]}
   */
  flatten(json: any[]) {
    return json.map(obj => Object.keys(obj)
                                 .map(key => obj[key])
                                 .reduce((acc, val) => Object.assign(acc, val), {}))
  }
}
