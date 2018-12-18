import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs/index";
import { Strategy } from "./shared/strategy.model";
import { flatMap } from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class StrategyService {

  constructor(private http: HttpClient) { }

  getStrategies(): Observable<Strategy[]> {
    return <Observable<Strategy[]>> this.http.get("strategy/all");
  }

  deactivate(id: number): Observable<void> {
    return this.http.get("strategy/deactivate/" + id).pipe(
      flatMap(() => Observable.create())
    );
  }

  activate(id: number): Observable<void> {
    return this.http.get("strategy/activate/" + id).pipe(
      flatMap(() => Observable.create())
    );
  }
}
