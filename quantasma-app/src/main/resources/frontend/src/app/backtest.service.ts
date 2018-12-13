import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class BacktestService {

  constructor(private http: HttpClient) { }

  public all() {
    return this.http.get("backtest/all");
  }
}
