import { Component, OnInit, ViewChild } from '@angular/core';
import { MatSort, MatTableDataSource } from '@angular/material';

@Component({
  selector: 'app-backtest-strategy-result',
  templateUrl: './backtest-strategy-result.component.html',
  styleUrls: ['./backtest-strategy-result.component.scss']
})
export class BacktestStrategyResultComponent implements OnInit {
  displayedColumns: string[] = [];
  dataSource?: MatTableDataSource<any[]>;

  @ViewChild(MatSort) sort?: MatSort;

  ngOnInit() {
  }

  updateTable(data: any[]) {
    this.displayedColumns = this.extractDataKeys(data);
    this.dataSource = new MatTableDataSource(data);
    this.dataSource.sort = <MatSort>this.sort;
  }

  private extractDataKeys(data: any[]) {
    return data.length > 0 ? Object.keys(data[0]) : [];
  }
}
