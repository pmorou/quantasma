import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs/index";
import { Strategy } from "../models/strategy.model";

@Injectable({
  providedIn: 'root'
})
export class StrategyService {

  constructor(private http: HttpClient) { }

  getStrategies(): Observable<Strategy[]> {
    return <Observable<Strategy[]>> this.http.get("api/strategy/all");
  }

  deactivate(id: number) {
    return this.http.patch("api/strategy/deactivate/" + id, {});
  }

  activate(id: number) {
    return this.http.patch("api/strategy/activate/" + id, {});
  }
}
