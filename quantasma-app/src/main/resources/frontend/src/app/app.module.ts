import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import { AppRoutingModule } from './app-routing.module';
import { SidebarComponent } from './sidebar/sidebar.component';
import { StatusViewComponent } from './status-view/status-view.component';
import { HttpClientModule } from "@angular/common/http";
import { QuotesComponent } from './quotes/quotes.component';
import { AccountStateComponent } from './account-state/account-state.component';
import { OpenedPositionsComponent } from './opened-positions/opened-positions.component';
import { BacktestViewComponent } from './backtest-view/backtest-view.component';
import { BacktestStrategyViewComponent } from './backtest-strategy-view/backtest-strategy-view.component';
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
    StatusViewComponent,
    QuotesComponent,
    AccountStateComponent,
    OpenedPositionsComponent,
    BacktestViewComponent,
    BacktestStrategyViewComponent,
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
