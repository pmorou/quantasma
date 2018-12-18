import { NgModule } from '@angular/core';
import { RouterModule, Routes } from "@angular/router";
import { StatusDashboardComponent } from "./status-dashboard/status-dashboard.component";
import { BacktestDashboardComponent } from "./backtest-dashboard/backtest-dashboard.component";
import { BacktestStrategyViewComponent } from "./backtest-strategy-view/backtest-strategy-view.component";

const routes: Routes = [
  {
    path: '',
    component: StatusDashboardComponent
  },
  {
    path: 'backtest',
    component: BacktestDashboardComponent
  },
  {
    path: 'backtest/:name',
    component: BacktestStrategyViewComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
