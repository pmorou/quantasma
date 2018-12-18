import { Component, OnInit } from '@angular/core';
import { StrategyService } from "../strategy.service";
import {flatMap} from "rxjs/operators";
import {Subject} from "rxjs/index";
import { Strategy } from "../shared/strategy.model";

@Component({
  selector: 'app-status-view',
  templateUrl: './status-view.component.html',
  styleUrls: ['./status-view.component.scss']
})
export class StatusViewComponent implements OnInit {
  strategies$: Strategy[] = [];

  private updateStrategiesSubject: Subject<void> = new Subject();

  constructor(private strategyService: StrategyService) { }

  ngOnInit() {
    this.updateStrategiesSubject.asObservable().pipe(
      flatMap(() => this.strategyService.getStrategies()))
    .subscribe(strategies => this.strategies$ = strategies);

    this.refreshStrategies();
  }

  refreshStrategies() {
    this.updateStrategiesSubject.next();
  }

  deactivateStrategy(id: number) {
    this.strategyService.deactivate(id).subscribe(() => {
      this.refreshStrategies();
    })
  }

  activateStrategy(id: number) {
    this.strategyService.activate(id).subscribe(() => {
      this.refreshStrategies();
    })
  }
}
