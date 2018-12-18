import {Component, OnInit, ViewChild} from '@angular/core';
import { ActivatedRoute } from "@angular/router";
import { BacktestService } from "../backtest.service";
import { Backtest } from "../shared/backtest.model";
import {BacktestStrategyResultComponent} from "../backtest-strategy-result/backtest-strategy-result.component";
import { concatMap } from "rxjs/internal/operators";
import { ParameterDescription } from "../shared/parameter-description.model";
import { Criterion } from "../shared/criterion.model";

@Component({
  selector: 'app-backtest-strategy-view',
  templateUrl: './backtest-strategy-view.component.html',
  styleUrls: ['./backtest-strategy-view.component.scss']
})
export class BacktestStrategyViewComponent implements OnInit {
  backtest?: Backtest;

  availableParameters: ParameterDescription[] = [];
  availableCriterions: Criterion[] = [];

  @ViewChild("backtestResult")
  backtestResult?: BacktestStrategyResultComponent;

  constructor(private route: ActivatedRoute, private backtestService: BacktestService) { }

  ngOnInit() {
    this.route.params.pipe(
      concatMap(params => this.backtestService.get(params.name)))
    .subscribe(value => {
      this.backtest = value;
      this.availableParameters = this.backtest.parameters;
    });
    this.backtestService.criterions().subscribe(value => {
      this.availableCriterions = value;
    })
  }

  testFinished($event: any[]) {
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
