import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { RouterModule } from "@angular/router";
import { AppRoutingModule } from './app-routing.module';
import { HttpClientModule } from "@angular/common/http";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { ReactiveFormsModule } from "@angular/forms";
import { MaterialModule } from './modules/material.module';
import { AppComponent } from './app.component';
import { SidebarComponent } from './components/sidebar/sidebar.component';
import { StatusViewComponent } from './components/status-view/status-view.component';
import { QuotesComponent } from './components/quotes/quotes.component';
import { AccountStateComponent } from './components/account-state/account-state.component';
import { OpenedPositionsComponent } from './components/opened-positions/opened-positions.component';
import { BacktestViewComponent } from './components/backtest-view/backtest-view.component';
import { BacktestStrategyViewComponent } from './components/backtest-strategy-view/backtest-strategy-view.component';
import { BacktestStrategySettingsComponent } from './components/backtest-strategy-settings/backtest-strategy-settings.component';
import { BacktestStrategyResultComponent } from './components/backtest-strategy-result/backtest-strategy-result.component';
import { HistoricalDataSummaryComponent } from './components/historical-data-summary/historical-data-summary.component';
import { HistoricalDataViewComponent } from './components/historical-data-view/historical-data-view.component';
import { HistoricalDataFeedComponent } from './components/historical-data-feed/historical-data-feed.component';

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
    BacktestStrategySettingsComponent,
    BacktestStrategyResultComponent,
    HistoricalDataSummaryComponent,
    HistoricalDataViewComponent,
    HistoricalDataFeedComponent
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
