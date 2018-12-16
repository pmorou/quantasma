import { Component, Input, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, Validators } from "@angular/forms";
import { BacktestService } from "../backtest.service";

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
  @Input("strategy")
  strategy: string = "";

  constructor(private fb: FormBuilder, private backtestService: BacktestService) { }

  backtestForm: FormGroup = this.fb.group({
    title: [],
    criterions: this.fb.array([this.fb.group({name:['', Validators.required], value:'true'})]),
    parameters: this.fb.array([this.fb.group({name:'', value:''})])
  });

  ngOnInit() {
  }

  onSubmit() {
    console.log(JSON.stringify(this.backtestForm.value));
    if (this.backtestForm.invalid) {
      console.log("Form invalid");
      return;
    }
    this.backtestService.test(this.strategy, JSON.stringify(this.backtestForm.value))
    .subscribe(value => {
      console.log("Tested");
    });
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
