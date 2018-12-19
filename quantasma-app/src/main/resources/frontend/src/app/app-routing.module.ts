import { NgModule } from '@angular/core';
import { RouterModule, Routes } from "@angular/router";
import { StatusViewComponent } from "./status-view/status-view.component";
import { BacktestViewComponent } from "./backtest-view/backtest-view.component";
import { BacktestStrategyViewComponent } from "./backtest-strategy-view/backtest-strategy-view.component";

const routes: Routes = [
  { path: '', component: StatusViewComponent },
  { path: 'backtest', component: BacktestViewComponent },
  { path: 'backtest/:name', component: BacktestStrategyViewComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
