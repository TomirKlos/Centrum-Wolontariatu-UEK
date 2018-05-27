import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator, MatSort } from '@angular/material';

import { VolunteerRequestVM, responseVolunteerRequestVM, Page } from '../../../shared/interfaces';
import { RequestService } from '../../shared/request.service';
import { ServerDataSource } from '../../../shared/server-data-source';
import { RequestDialogService } from '../../shared/request-dialog.service';

import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { MyRequestsService } from './my-requests.service';

import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { ApplyService } from './view-apply-request/apply-request.service';

@Component({
  selector: 'app-my-requests',
  templateUrl: './my-requests.component.html',
  styleUrls: [ './my-requests.component.scss' ]
})
export class MyRequestsComponent implements OnInit, AfterViewInit {
  dataSource: ServerDataSource<VolunteerRequestVM>;
  dataSourceBadge: ServerDataSource<responseVolunteerRequestVM>;
  dataSourceApplications: ServerDataSource<responseVolunteerRequestVM>;
  columnsToDisplay = [ 'id', 'accepted', 'title', 'application', 'editVr' ];

  badgeCount = 5;

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  constructor(
    private _requestService: RequestService, 
    private _applyService: ApplyService,
    private _dialogService: RequestDialogService, 
    private _http: HttpClient,
    private _myRequestsService: MyRequestsService,
  ) {
  }

  ngOnInit() {
    this.dataSource = new ServerDataSource<VolunteerRequestVM>(this._requestService, this.paginator, this.sort, "VolunteerRequest");
    this.dataSource.relativePathToServerResource = 'mine';
    this.dataSource.loadPage();

  }

  ngAfterViewInit() {
    this.dataSource.initAfterViewInit();
  }

  openDialog(request: VolunteerRequestVM): void {
    this._dialogService.open(request);
  }

  showApplications(request: number){
    this._dialogService.openApplicationsPanel(request);
  }

  getBadgeCount(id: number){
    this._myRequestsService.get(id).debounceTime(10000).subscribe((data)=>{
      return data;
    });
  }
}
