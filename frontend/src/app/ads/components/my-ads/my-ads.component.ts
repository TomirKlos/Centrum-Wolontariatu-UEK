import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator, MatSort } from '@angular/material';

import { VolunteerRequestVM, responseVolunteerRequestVM, Page, VolunteerAdVM } from '../../../shared/interfaces';

import { ServerDataSource } from '../../../shared/server-data-source';


import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';


import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { AdService } from '../../shared/ad.service';
import { RequestDialogService } from '../../../requests/shared/request-dialog.service';
import { MyAdsService } from './my-ads.service';
import { AdDialogService } from '../../shared/ad-dialog.service';


@Component({
  selector: 'app-my-ads',
  templateUrl: './my-ads.component.html',
  styleUrls: [ './my-ads.component.scss' ]
})
export class MyAdssComponent implements OnInit, AfterViewInit {
  dataSource: ServerDataSource<VolunteerAdVM>;
 // dataSourceBadge: ServerDataSource<responseVolunteerRequestVM>;
//  dataSourceApplications: ServerDataSource<responseVolunteerRequestVM>;
  columnsToDisplay = [ 'id', 'accepted', 'title', 'application', 'editVr' ];

  badgeCount = 5;

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  constructor(
    private _adService: AdService, 
   // private _applyService: ApplyService,
    private _dialogService: AdDialogService,
    private _http: HttpClient,
    private _myAdsService: MyAdsService,
  ) {
  }

  ngOnInit() {
    this.dataSource = new ServerDataSource<VolunteerAdVM>(this._adService, this.paginator, this.sort, "volunteerAd");
    this.dataSource.relativePathToServerResource = 'mine';
    this.dataSource.loadPage();

  }

  ngAfterViewInit() {
    this.dataSource.initAfterViewInit();
  }

  openDialog(request: VolunteerAdVM): void {
    this._dialogService.open(request);
  }

  showApplications(request: number){
    this._dialogService.openApplicationsPanel(request);
  } 


}
