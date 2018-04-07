import { HttpClient } from '@angular/common/http';
import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator, MatSort } from '@angular/material';
import { tap } from 'rxjs/operators';

import { SnackBarService } from '../../shared/snack-bar.service';
import { UsersService } from './users.service';
import { GenericDataSource } from "../../shared/GenericDataSource";
import { merge } from "rxjs/observable/merge";

@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrls: [ './users.component.scss' ],
  providers: [
    UsersService
  ]
})
export class UsersComponent implements OnInit, AfterViewInit {
  usersData: GenericDataSource;
  columnsToDisplay = [ 'id', 'activated', 'email', 'delete' ];
  totalElements = 0;

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  constructor(private _http: HttpClient, private _usersService: UsersService, private _sb: SnackBarService) {
  }

  ngOnInit() {
    this.usersData = new GenericDataSource(this._usersService);
    this.usersData.loadPage();

    this._usersService.getPage().subscribe(d => {
      if (d && d.totalElements) {
        this.totalElements = d.totalElements;
      }
    });
  }

  ngAfterViewInit() {
    merge(this.sort.sortChange, this.paginator.page)
      .pipe(
        tap(() => this._loadUsersPage())
      )
      .subscribe();
  }

  private _loadUsersPage() {
    this.usersData.loadPage(
      this.paginator.pageIndex,
      this.paginator.pageSize,
      { name: this.sort.active, value: this.sort.direction }
    );
  }

  deleteUser(id: number) {
    this._usersService.delete(id).subscribe(
      () => {
        this._sb.open('Usunięto konto');
        this._loadUsersPage();
      },
      () => this._sb.warning()
    );
  }

  activateUser(id: number) {
    this._usersService.activate(id).subscribe(
      () => {
        this._sb.open('Zapisano');
        this._loadUsersPage();
      },
      () => this._sb.warning()
    );
  }
}
