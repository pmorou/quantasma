import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import {Criterion} from "./shared/criterion.model";
import {map} from "rxjs/internal/operators";
import {Observable} from "rxjs/index";
import {Backtest} from "./shared/backtest.model";

@Injectable({
  providedIn: 'root'
})
export class BacktestService {

  constructor(private http: HttpClient) { }

  public all(): Observable<Backtest[]> {
    return <Observable<Backtest[]>> this.http.get("backtest/all");
  }

  public get(name: string): Observable<Backtest> {
    return <Observable<Backtest>> this.http.get("backtest/" + name);
  }

  public test(name: string, json: string) {
    return this.http.post("backtest/" + name, json);
  }

  public criterions(): Observable<Criterion[]> {
    return this.http.get("backtest/criterions").pipe(
      map(arr => (<string[]> arr).map(str => <Criterion>{name: str}))
    );
  }
}
