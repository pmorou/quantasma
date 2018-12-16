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

  public get(name: string) {
    return this.http.get("backtest/" + name);
  }

  public test(name: string, json: string) {
    return this.http.post("backtest/" + name, json);
  }
}
