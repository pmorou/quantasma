import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class StrategyService {

  constructor(private http: HttpClient) { }

  getStrategies() {
    return this.http.get("strategy/all");
  }
}
