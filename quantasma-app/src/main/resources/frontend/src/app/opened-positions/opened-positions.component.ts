import { Component, OnInit } from '@angular/core';
import { EventsService } from "../events.service";
import { OpenedPosition } from "../../shared/opened-position.model";

@Component({
  selector: 'app-opened-positions',
  templateUrl: './opened-positions.component.html',
  styleUrls: ['./opened-positions.component.scss']
})
export class OpenedPositionsComponent implements OnInit {

  public openedPositions$: OpenedPosition[] = [];

  constructor(private events: EventsService) {
  }

  ngOnInit() {
    this.events.openedPositions().subscribe(value => {
      this.openedPositions$ = value;
    })
  }

}
