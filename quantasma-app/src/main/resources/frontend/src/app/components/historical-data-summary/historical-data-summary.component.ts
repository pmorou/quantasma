import { Component, OnInit } from '@angular/core';
import { HistoricalDataService } from "../../services/historical-data.service";
import { HistoricalDataSummaryHolder } from "../../models/historical-data-summary.model";

@Component({
  selector: 'app-historical-data-summary',
  templateUrl: './historical-data-summary.component.html',
  styleUrls: ['./historical-data-summary.component.scss']
})
export class HistoricalDataSummaryComponent implements OnInit {
  dataSummary?: HistoricalDataSummaryHolder;

  constructor(private service: HistoricalDataService) { }

  ngOnInit() {
    this.service.dataSummary().subscribe(value =>
      this.dataSummary = new HistoricalDataSummaryHolder(value)
    );
  }

}
