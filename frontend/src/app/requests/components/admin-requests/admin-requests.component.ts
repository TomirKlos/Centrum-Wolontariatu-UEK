import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator, MatSort } from '@angular/material';

import { VolunteerRequestVM } from '../../../shared/interfaces';

import { AdminRequestsService } from './admin-requests.service';
import { RequestDialogService } from '../../shared/request-dialog.service';
import { ServerDataSource } from '../../../shared/server-data-source';

@Component({
  selector: 'app-admin-requests',
  templateUrl: './admin-requests.component.html',
  providers: [ AdminRequestsService ]
})
export class AdminRequestsComponent implements OnInit, AfterViewInit {
  dataSource: ServerDataSource<VolunteerRequestVM>;
  columnsToDisplay = [ 'id', 'accepted', 'title', 'delete', 'editVr' ];

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  constructor(
    private _adminRequestService: AdminRequestsService,
    private _dialogService: RequestDialogService
  ) {
  }

  ngOnInit() {
    this.dataSource = new ServerDataSource(this._adminRequestService, this.paginator, this.sort);
    this.dataSource.loadPage();
  }

  ngAfterViewInit() {
    this.dataSource.initAfterViewInit();
  }

  changeAccepted(id: number) {
    this._adminRequestService.accept(id).subscribe(() => this.dataSource.loadPage());
  }

  deleteVr(id: number) {
    this._adminRequestService.delete(id).subscribe(() => this.dataSource.loadPage());
  }

  openDialog(request: VolunteerRequestVM): void {
    this._dialogService.open(request);
  }

}
