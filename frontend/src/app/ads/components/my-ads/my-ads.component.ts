import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import {MatPaginator, MatSort, MatSortable} from '@angular/material';

import {  VolunteerAdVM } from '../../../shared/interfaces';
import { ServerDataSource } from '../../../shared/server-data-source';
import { HttpClient, HttpParams } from '@angular/common/http';
import { AdService } from '../../shared/ad.service';
import { MyAdsService } from './my-ads.service';
import { AdDialogService } from '../../shared/ad-dialog.service';
import {SnackBarService} from '../../../shared/snack-bar.service';


@Component({
  selector: 'app-my-ads',
  templateUrl: './my-ads.component.html',
  styleUrls: [ './my-ads.component.scss' ]
})
export class MyAdssComponent implements OnInit, AfterViewInit {
  dataSource: ServerDataSource<VolunteerAdVM>;
  columnsToDisplay = [ 'id', 'accepted', 'title', 'application', 'editVr', 'expire' ];
  totalElements: number;
  pageSize = 5;

  badgeData: BadgeData[] = [];
  badgePrepared: boolean = false;

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  constructor(
    private _adService: AdService,
    private _dialogService: AdDialogService,
    private _http: HttpClient,
    private _myAdsService: MyAdsService,
    private _sb: SnackBarService
  ) {
  }

  ngOnInit() {
    this.sort.sort(<MatSortable>({id: 'id', start: 'desc'}));
    this.paginator.pageSize = this.pageSize;
    this.dataSource = new ServerDataSource<VolunteerAdVM>(this._adService, this.paginator, this.sort, 'volunteerAd');
    this.dataSource.relativePathToServerResource = 'mine';
    this.dataSource.loadPage();

    this.dataSource.connectToSourceElementsNumber().subscribe(d => {
      this.totalElements = d;
    });

    this.getIds();
    this.badgePrepared = true;

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
  getIds(){
    this._myAdsService.getIDs().subscribe((data)=>{
      (data as number[]).forEach(element => {

        this._myAdsService.get(element).debounceTime(10000).subscribe((data)=>{
          var badgeData = new BadgeData(element, data);
          this.badgeData.push(badgeData);
          return data;
        });
      });
      return data;
    });
  }

  getBadgeById(id:number){
    for (var i = 0; i < this.badgeData.length; i++) {
      if (this.badgeData[i]["id"] === id) {
        return this.badgeData[i];
      }
    }
    return null;
  }

  clearCount(id: number){
    for (var i = 0; i < this.badgeData.length; i++) {
      if (this.badgeData[i]["id"] === id) {
        this.badgeData[i]["id"] = 0;
      }
    }
    return null;
  }

  setExpired(id: number) {
    this._myAdsService.expire(id).subscribe(
      () => {
        this._sb.open('Ogloszenie zostalo przedawnione');
        this.dataSource.loadPage();
      },
      () => this._sb.warning()
    );
  }

}

export class BadgeData{
  private id:number;
  private text: string;

  constructor(id,text) {
    this.id = id;
    this.text = text;
  }
}
