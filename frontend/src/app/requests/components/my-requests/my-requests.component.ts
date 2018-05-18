import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator, MatSort } from '@angular/material';

import { VolunteerRequestVM } from '../../../shared/interfaces';
import { RequestService } from '../../shared/request.service';
import { ServerDataSource } from '../../../shared/server-data-source';
import { RequestDialogService } from '../../shared/request-dialog.service';

@Component({
  selector: 'app-my-requests',
  templateUrl: './my-requests.component.html',
})
export class MyRequestsComponent implements OnInit, AfterViewInit {
  dataSource: ServerDataSource<VolunteerRequestVM>;
  columnsToDisplay = [ 'id', 'accepted', 'title', 'editVr' ];

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  constructor(private _requestService: RequestService, private _dialogService: RequestDialogService) {
  }

  ngOnInit() {
    this.dataSource = new ServerDataSource<VolunteerRequestVM>(this._requestService, this.paginator, this.sort);
    this.dataSource.relativePathToServerResource = 'mine';
    this.dataSource.loadPage();
  }

  ngAfterViewInit() {
    this.dataSource.initAfterViewInit();
  }

  openDialog(request: VolunteerRequestVM): void {
    this._dialogService.open(request);
  }

}
