import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import { AppRoutingModule } from './app-routing.module';
import { SidebarComponent } from './sidebar/sidebar.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { HttpClientModule } from "@angular/common/http";
import { QuotesComponent } from './quotes/quotes.component';
import { AccountStateComponent } from './account-state/account-state.component';
import { OpenedPositionsComponent } from './opened-positions/opened-positions.component';
import { BacktestComponent } from './backtests/backtest.component';
import { BacktestStrategyComponent } from './backtest-strategy/backtest-strategy.component';
import { RouterModule } from "@angular/router";

@NgModule({
  declarations: [
    AppComponent,
    SidebarComponent,
    DashboardComponent,
    QuotesComponent,
    AccountStateComponent,
    OpenedPositionsComponent,
    BacktestComponent,
    BacktestStrategyComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    RouterModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
