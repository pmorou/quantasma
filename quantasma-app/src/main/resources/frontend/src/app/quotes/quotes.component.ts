import { Component, OnInit } from '@angular/core';
import { EventsService } from "../events.service";
import { Quote } from "../../shared/quote.model";

@Component({
  selector: 'app-quotes',
  templateUrl: './quotes.component.html',
  styleUrls: ['./quotes.component.scss']
})
export class QuotesComponent implements OnInit {

  constructor(private events: EventsService) { }

  ngOnInit() {
    this.events.quotes().subscribe((quote: Quote) => QuotesComponent.renderQuote(quote));
  }

  private static renderQuote(quote: Quote): void {
    let li = document.getElementById("quote-" + quote.symbol);
    if (li == null) {
      li = document.createElement("li");
      li.id = "quote-" + quote.symbol;
      document.getElementsByClassName("quotes__list")[0].appendChild(li);
    }
    li.innerHTML =
      "<b>" + quote.symbol + "</b>" +
      "</br>bid: " + quote.bid +
      "</br>ask: " + quote.ask;
  };

}
