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
import { BacktestStrategyFormComponent } from './backtest-strategy-form/backtest-strategy-form.component';
import { RouterModule } from "@angular/router";
import { MaterialModule } from './material.module';
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { ReactiveFormsModule } from "@angular/forms";
import { BacktestStrategyResultComponent } from './backtest-strategy-result/backtest-strategy-result.component';

@NgModule({
  declarations: [
    AppComponent,
    SidebarComponent,
    DashboardComponent,
    QuotesComponent,
    AccountStateComponent,
    OpenedPositionsComponent,
    BacktestComponent,
    BacktestStrategyComponent,
    BacktestStrategyFormComponent,
    BacktestStrategyResultComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    RouterModule,
    ReactiveFormsModule,
    MaterialModule,
    BrowserAnimationsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
