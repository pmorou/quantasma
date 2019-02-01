import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, Validators } from "@angular/forms";
import { BacktestService } from "../../services/backtest.service";
import { ParameterDescription } from "../../models/parameter-description.model";
import { Criterion } from "../../models/criterion.model";
import { TimeWindow } from "../../models/time-window.model";

@Component({
  selector: 'app-backtest-strategy-settings',
  templateUrl: './backtest-strategy-settings.component.html',
  styleUrls: ['./backtest-strategy-settings.component.scss']
})
export class BacktestStrategySettingsComponent implements OnInit {
  @Input("params")
  availableParameters: ParameterDescription[] = [];
  @Input("crits")
  availableCriterions: Criterion[] = [];
  @Input("backtestName")
  backtestName: string = "";

  @Output()
  testFinished = new EventEmitter();

  backtestForm: FormGroup = FormGroup.prototype;
  status: string = "ready";
  availableTimeWindows: TimeWindow[] = [
    {name: '1 day', value: 'P1D'},
    {name: '1 week', value: 'P1W'},
    {name: '1 month', value: 'P1M'},
    {name: '1 year', value: 'P1Y'}
  ];

  constructor(private formBuilder: FormBuilder, private backtestService: BacktestService) {
    this.backtestForm = this.formBuilder.group({
      title: [],
      time: this.formBuilder.group({from: {}, window: {}}),
      criterions: this.formBuilder.array([this.formBuilder.group({name: '', value: ''})]),
      parameters: this.formBuilder.array([this.formBuilder.group({name: '', value: ''})])
    });
  }

  ngOnInit() {
  }

  onSubmit() {
    if (this.backtestForm.invalid) {
      this.status = "invalid form.";
      return;
    }

    this.status = "loading...";

    this.backtestService.test(this.backtestName, JSON.stringify(this.backtestForm.value)).subscribe(value => {
      this.testFinished.emit(value);
      this.status = "done.";
    });
  }

  get parameters() {
    return this.backtestForm.get('parameters') as FormArray;
  }

  addParameter(name: string) {
    this.parameters.push(this.formBuilder.group({name: [name, Validators.required], value: ['', Validators.required]}));
  }

  get criterions() {
    return this.backtestForm.get('criterions') as FormArray;
  }

  addCriterion(name: string) {
    this.criterions.push(this.formBuilder.group({name: [name, Validators.required], value: ['true', Validators.required]}));
  }

  deleteCriterions(index: number) {
    this.criterions.removeAt(index);
  }

  renderForm() {
    this.backtestForm = this.formBuilder.group({
      title: [],
      time: this.formBuilder.group({
        from: ['', Validators.required],
        window: ['', Validators.required]
      }),
      criterions: this.formBuilder.array([this.formBuilder.group({name: ''})]
      ),
      parameters: this.formBuilder.array(
        this.availableParameters
        .map(param => Object.assign({value: ''}, {name: param.name}))
        .map(obj => this.formBuilder.group(obj))
      )
    });
  }
}
