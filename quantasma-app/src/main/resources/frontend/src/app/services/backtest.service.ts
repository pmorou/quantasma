import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Criterion } from "../models/criterion.model";
import { map } from "rxjs/internal/operators";
import { Observable } from "rxjs/index";
import { Backtest } from "../models/backtest.model";

@Injectable({
  providedIn: 'root'
})
export class BacktestService {

  constructor(private http: HttpClient) { }

  public all(): Observable<Backtest[]> {
    return <Observable<Backtest[]>> this.http.get("api/backtest/all");
  }

  public get(name: string): Observable<Backtest> {
    return <Observable<Backtest>> this.http.get("api/backtest/" + name);
  }

  public test(name: string, json: string) {
    return this.http.post("api/backtest/" + name, json,
      { headers: new HttpHeaders({ 'Content-Type': 'application/json' }) }
    );
  }

  public criterions(): Observable<Criterion[]> {
    return this.http.get("api/backtest/criterions").pipe(
      map(arr => (<string[]> arr).map(str => <Criterion>{name: str}))
    );
  }
}
