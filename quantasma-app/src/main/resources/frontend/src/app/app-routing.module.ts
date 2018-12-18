import { NgModule } from '@angular/core';
import { RouterModule, Routes } from "@angular/router";
import { DashboardComponent } from "./dashboard/dashboard.component";
import { BacktestDashboardComponent } from "./backtest-dashboard/backtest-dashboard.component";
import { BacktestStrategyComponent } from "./backtest-strategy/backtest-strategy.component";

const routes: Routes = [
  {
    path: '',
    component: DashboardComponent
  },
  {
    path: 'backtest',
    component: BacktestDashboardComponent
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
