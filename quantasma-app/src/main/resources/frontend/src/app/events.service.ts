import { Injectable } from '@angular/core';
import { Quote } from "../shared/quote.model";
import { Observable, Subject } from "rxjs/index";

@Injectable({
  providedIn: 'root'
})
export class EventsService {

  private quotesSource: EventSource;
  private quotesSubject: Subject<Quote> = new Subject();

  constructor() {
    this.quotesSource = new EventSource('events/quotes');
    this.quotesSource.addEventListener('quote-event', ((event: MessageEvent) => {
      this.quotesSubject.next(JSON.parse(event.data));
    }) as (event: Event) => void);
    this.quotesSource.onerror = (evt) => EventsService._onSseError(evt);
  }

  private static _onSseError(e: any): void {
    console.log("SSE Event failure: ", e);
  }

  public quotes(): Observable<Quote> {
    return this.quotesSubject.asObservable();
  }
}
