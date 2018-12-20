import { NgModule } from '@angular/core';

import {
  MatTableModule,
  MatSortModule, 
} from '@angular/material';

@NgModule({
  imports: [
      MatTableModule,
      MatSortModule, 
    ],
  exports: [
      MatTableModule,
      MatSortModule, 
    ]
})
export class MaterialModule { }