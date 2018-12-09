import { Component, OnInit } from '@angular/core';
import { NavigationEnd, Router } from "@angular/router";
import { filter, map } from "rxjs/internal/operators";

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss']
})
export class SidebarComponent implements OnInit {
  currentUrl: string = "";

  constructor(private router: Router) {
    this.router.events.pipe(
      filter(event => event instanceof NavigationEnd),
      map(value => <NavigationEnd> value)
    ).subscribe(
      (_: NavigationEnd) => this.currentUrl = _.url
    )
  }

  ngOnInit() {
  }

}
