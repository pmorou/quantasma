import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { HistoricalDataService } from "../../services/historical-data.service";

@Component({
  selector: 'app-historical-data-feed',
  templateUrl: './historical-data-feed.component.html',
  styleUrls: ['./historical-data-feed.component.scss']
})
export class HistoricalDataFeedComponent implements OnInit {
  updateForm: FormGroup = FormGroup.prototype;
  status: string = "ready";

  constructor(private formBuilder: FormBuilder, private service: HistoricalDataService) {
    this.updateForm = this.formBuilder.group({
      symbol: ['', Validators.required],
      barPeriod: ['', Validators.required],
      fromDate: ['', Validators.required],
      toDate: ['', Validators.required]
    })
  }

  ngOnInit() {
  }

  update() {
    if (this.updateForm.invalid) {
      this.status = "invalid form.";
      return;
    }

    this.service.update(JSON.stringify(this.updateForm.value)).subscribe(response => this.status = response.status);
  }
}
