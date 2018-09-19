import { HttpClient } from '@angular/common/http';
import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import {MatPaginator, MatSort, MatSortable} from '@angular/material';
import { tap } from 'rxjs/operators';
import { merge } from 'rxjs/observable/merge';

import { SnackBarService } from '../../shared/snack-bar.service';
import { UsersService } from './users.service';
import { GenericDataSource } from '../../shared/GenericDataSource';
import { User } from '../../shared/interfaces';

@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrls: [ './users.component.scss' ],
  providers: [
    UsersService
  ]
})
export class UsersComponent implements OnInit, AfterViewInit {
  usersData: GenericDataSource<User>;
  columnsToDisplay = [ 'id', 'activated', 'email', 'delete' ];
  totalElements = 0;
  pageSize = 5;

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  constructor(private _http: HttpClient, private _usersService: UsersService, private _sb: SnackBarService) {
  }

  ngOnInit() {
    this.sort.sort(<MatSortable>({id: 'id', start: 'desc'}));
    this.paginator.pageSize = this.pageSize;
    this.usersData = new GenericDataSource(this._usersService);
    this._loadUsersPage();

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
      { name: 'page', value: this.paginator.pageIndex },
      { name: 'size', value: this.paginator.pageSize },
      { name: 'sort', value: this.sort.active + ',' + this.sort.direction }
    );
  }

  deleteUser(id: number) {
    this._usersService.delete(id).subscribe(() => this._loadUsersPage());
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
