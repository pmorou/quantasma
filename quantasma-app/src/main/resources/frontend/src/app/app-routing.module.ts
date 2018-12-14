import { NgModule } from '@angular/core';
import { RouterModule, Routes } from "@angular/router";
import { DashboardComponent } from "./dashboard/dashboard.component";
import { BacktestComponent } from "./backtests/backtest.component";
import { BacktestStrategyComponent } from "./backtest-strategy/backtest-strategy.component";

const routes: Routes = [
  {
    path: '',
    component: DashboardComponent
  },
  {
    path: 'backtest',
    component: BacktestComponent
  },
  {
    path: 'backtest/:name',
    component: BacktestStrategyComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
