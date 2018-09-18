import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import {MatPaginator, MatSort, MatSortable} from '@angular/material';

import {VolunteerRequestVM} from '../../../shared/interfaces';
import {RequestService} from '../../shared/request.service';
import {ServerDataSource} from '../../../shared/server-data-source';
import {RequestDialogService} from '../../shared/request-dialog.service';

import {HttpClient} from '@angular/common/http';
import {MyRequestsService} from './my-requests.service';

import {ApplyService} from './view-apply-request/apply-request.service';
import {SnackBarService} from '../../../shared/snack-bar.service';

@Component({
  selector: 'app-my-requests',
  templateUrl: './my-requests.component.html',
  styleUrls: ['./my-requests.component.scss']
})
export class MyRequestsComponent implements OnInit, AfterViewInit {
  dataSource: ServerDataSource<VolunteerRequestVM>;
  columnsToDisplay = ['id', 'accepted', 'title', 'application', 'editVr', 'expire'];

  totalElements: number;

  badgeTemp: String[] = [];
  badgeData: BadgeData[] = [];

  badgePrepared: boolean = false;

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  constructor(private _requestService: RequestService,
              private _applyService: ApplyService,
              private _dialogService: RequestDialogService,
              private _http: HttpClient,
              private _myRequestsService: MyRequestsService,
              private _sb: SnackBarService) {
  }

  ngOnInit() {
    this.sort.sort(<MatSortable>({id: 'id', start: 'desc'}));
    this.dataSource = new ServerDataSource<VolunteerRequestVM>(this._requestService, this.paginator, this.sort, 'volunteerRequest');
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

  openDialog(request: VolunteerRequestVM): void {
    this._dialogService.open(request);
  }

  showApplications(request: number) {
    this._dialogService.openApplicationsPanel(request);
  }

  getBadgeCount(id: any) {
    this._myRequestsService.get(id).debounceTime(10000).subscribe((data) => {
      this.badgeTemp.push(data as string);
      return data;
    });
  }

  getIds() {
    this._myRequestsService.getIDs().subscribe((data) => {
      (data as number[]).forEach(element => {

        this._myRequestsService.get(element).debounceTime(10000).subscribe((data) => {
          var badgeData = new BadgeData(element, data);
          this.badgeData.push(badgeData);
          return data;
        });
      });
      return data;
    });
  }

  getBadgeById(id: number) {
    for (var i = 0; i < this.badgeData.length; i++) {
      if (this.badgeData[i]['id'] === id) {
        return this.badgeData[i];
      }
    }
    return null;
  }


  clearCount(id: number) {
    for (var i = 0; i < this.badgeData.length; i++) {
      if (this.badgeData[i]['id'] === id) {
        this.badgeData[i]['id'] = 0;
      }
    }
    return null;
  }

  setExpired(id: number) {
    this._myRequestsService.expire(id).subscribe(
      () => {
        this._sb.open('Ogloszenie zostalo przedawnione');
        this.dataSource.loadPage();
      },
      () => this._sb.warning()
    );
  }

}

export class BadgeData {
  private id: number;
  private text: string;

  constructor(id, text) {
    this.id = id;
    this.text = text;
  }
}
