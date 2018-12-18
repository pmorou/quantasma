import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs/index";
import { Strategy } from "./shared/strategy.model";

@Injectable({
  providedIn: 'root'
})
export class StrategyService {

  constructor(private http: HttpClient) { }

  getStrategies(): Observable<Strategy[]> {
    return <Observable<Strategy[]>> this.http.get("strategy/all");
  }

  deactivate(id: number) {
    return this.http.patch("strategy/deactivate/" + id, {});
  }

  activate(id: number) {
    return this.http.patch("strategy/activate/" + id, {});
  }
}
