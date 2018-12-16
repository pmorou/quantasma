import { Component, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, Validators } from "@angular/forms";

@Component({
  selector: 'app-backtest-strategy-form',
  templateUrl: './backtest-strategy-form.component.html',
  styleUrls: ['./backtest-strategy-form.component.scss']
})
export class BacktestStrategyFormComponent implements OnInit {

  @Input("params")
  availableParameters: Object[] = [];
  @Input("crits")
  availableCriterions: string[] = [];

  constructor(private fb: FormBuilder) { }

  backtestForm: FormGroup = this.fb.group({
    title: [],
    criterions: this.fb.array([this.fb.group({name:['', Validators.required], value:'true'})]),
    parameters: this.fb.array([this.fb.group({name:'', value:''})])
  });

  ngOnInit() {
  }

  onSubmit() {
    console.log(this.backtestForm.invalid);
    console.log(JSON.stringify(this.backtestForm.value));
  }

  get parameters() {
    return this.backtestForm.get('parameters') as FormArray;
  }

  addParameter(name: string) {
    this.parameters.push(this.fb.group({name: [name, Validators.required], value: ['', Validators.required]}));
  }

  deleteParameter(index: number) {
    this.parameters.removeAt(index);
  }

  get criterions() {
    return this.backtestForm.get('criterions') as FormArray;
  }

  addCriterion(name: string) {
    this.criterions.push(this.fb.group({name: [name, Validators.required], value: ['true', Validators.required]}));
  }

  deleteCriterions(index: number) {
    this.criterions.removeAt(index);
  }

}
