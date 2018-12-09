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
    this.updateCurrency();
  }

  updateCurrency(): void {
    this.events.quotes().subscribe((value: Quote) => {
        var li = document.getElementById("quote-" + value.symbol);
        if (li == null) {
          li = document.createElement("li");
          li.id = "quote-" + value.symbol;
          document.getElementsByClassName("quotes__list")[0].appendChild(li);
        }
        li.innerHTML =
          "<b>" + value.symbol + "</b>" +
          "</br>bid: " + value.bid +
          "</br>ask: " + value.ask;
      }
    );
  }

}
