import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { HistoricalDataSummary } from "../models/historical-data-summary.model";
import { Observable } from "rxjs/index";
import { map } from "rxjs/internal/operators";

@Injectable({
  providedIn: 'root'
})
export class HistoricalDataService {

  constructor(private http: HttpClient) { }

  dataSummary(): Observable<HistoricalDataSummary> {
    return <Observable<HistoricalDataSummary>> this.http.get("api/backtest/ticks/summary").pipe(
      map(response => (<any>response)['data']));
  }

  update(json: string): Observable<any> {
    return <Observable<any>> this.http.put("api/backtest/ticks", json,
      {headers: new HttpHeaders({'Content-Type': 'application/json'})});
  }
}
