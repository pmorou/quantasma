import { Component, OnInit } from '@angular/core';
import { StrategyService } from "../strategy.service";
import {map, tap} from "rxjs/operators";
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

  updateStrategiesSubject: Subject<void> = new Subject();
  deactivateStrategySubject: Subject<number> = new Subject();
  activateStrategySubject: Subject<number> = new Subject();

  constructor(private strategyService: StrategyService) { }

  ngOnInit() {
    this.updateStrategiesSubject.asObservable().pipe(
      flatMap(() => this.strategyService.getStrategies()),
      tap(strategies => this.strategies$ = strategies))
    .subscribe();

    this.deactivateStrategySubject.asObservable().pipe(
      map((id: any) => this.strategyService.deactivate(id)),
      tap(() => this.updateStrategies))
    .subscribe();

    this.activateStrategySubject.asObservable().pipe(
      map((id: any) => this.strategyService.activate(id)),
      tap(() => this.updateStrategies))
    .subscribe();

    this.updateStrategies();
  }

  updateStrategies() {
    this.updateStrategiesSubject.next();
  }

  deactivateStrategy(id: number) {
    this.deactivateStrategySubject.next(id);
  }

  activateStrategy(id: number) {
    this.activateStrategySubject.next(id);
  }
}
