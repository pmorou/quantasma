import { Component, OnInit } from '@angular/core';
import { StrategyService } from "../strategy.service";
import {map, tap} from "rxjs/operators";
import {flatMap} from "rxjs/operators";
import {Subject} from "rxjs/index";

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
  strategies$: any = [];

  updateStrategiesSubject: Subject<void> = new Subject();
  deactivateStrategySubject: Subject<any> = new Subject();
  activateStrategySubject: Subject<any> = new Subject();

  constructor(private strategyService: StrategyService) { }

  ngOnInit() {
    this.updateStrategiesSubject.asObservable().pipe(
      flatMap(() => this.strategyService.getStrategies()),
      tap((strategies: any[]) => this.strategies$ = strategies))
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

  deactivateStrategy(id: any) {
    this.deactivateStrategySubject.next(id);
  }

  activateStrategy(id: any) {
    this.activateStrategySubject.next(id);
  }
}
