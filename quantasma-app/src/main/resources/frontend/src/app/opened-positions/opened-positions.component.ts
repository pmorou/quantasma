import { Component, OnDestroy, OnInit } from '@angular/core';
import { EventsService } from "../events.service";
import { OpenedPosition } from "../shared/opened-position.model";
import { Subscription } from "rxjs/index";

@Component({
  selector: 'app-opened-positions',
  templateUrl: './opened-positions.component.html',
  styleUrls: ['./opened-positions.component.scss']
})
export class OpenedPositionsComponent implements OnInit, OnDestroy {

  openedPositions$: OpenedPosition[] = [];
  subscription: Subscription = Subscription.EMPTY;

  constructor(private events: EventsService) {
  }

  ngOnInit() {
    this.subscription = this.events.openedPositions().subscribe(value => {
      this.openedPositions$ = value;
    })
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }

}
