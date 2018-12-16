import { Component, OnInit } from '@angular/core';
import { StrategyService } from "../strategy.service";

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
  strategies$: any = [];

  constructor(private strategyService: StrategyService) { }

  ngOnInit() {
    this.getStrategies();
  }

  getStrategies() {
    this.strategyService.getStrategies().subscribe(
      data => this.strategies$ = data
    );
  }

  deactivateStrategy(id: any) {
    this.strategyService.deactivate(id).subscribe(() => this.getStrategies());
  }

  activateStrategy(id: any) {
    this.strategyService.activate(id).subscribe(() => this.getStrategies());
  }
}
