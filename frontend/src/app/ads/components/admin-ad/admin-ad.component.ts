import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator, MatSort } from '@angular/material';

import { VolunteerRequestVM, VolunteerAdVM } from '../../../shared/interfaces';


import { ServerDataSource } from '../../../shared/server-data-source';
import { RequestDialogService } from '../../../requests/shared/request-dialog.service';
import { AdminAdsService } from './admin-ad.service';

@Component({
  selector: 'app-admin-ad',
  templateUrl: './admin-ad.component.html',
  providers: [ AdminAdsService ]
})
export class AdminAdComponent implements OnInit, AfterViewInit {
  dataSource: ServerDataSource<VolunteerAdVM>;
  columnsToDisplay = [ 'id', 'accepted', 'title', 'delete', 'editVr' ];

  totalElements: number;

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  constructor(
    private _adminAdService: AdminAdsService,
    private _dialogService: RequestDialogService
  ) {
  }

  ngOnInit() {
    this.dataSource = new ServerDataSource(this._adminAdService, this.paginator, this.sort, 'volunteerAd');
    this.dataSource.loadPage();

    this.dataSource.connectToSourceElementsNumber().subscribe(d => {
      this.totalElements = d;
    });
  }

  ngAfterViewInit() {
    this.dataSource.initAfterViewInit();
  }

  changeAccepted(id: number) {
    this._adminAdService.accept(id).subscribe(() => this.dataSource.loadPage());
  }

  deleteAd(id: number) {
    this._adminAdService.delete(id).subscribe(() => this.dataSource.loadPage());
  }

  openDialog(request: VolunteerAdVM): void {
    this._dialogService.open(request);
  }

}
