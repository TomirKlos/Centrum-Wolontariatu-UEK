import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { tap } from "rxjs/operators";
import { MatPaginator, MatSort } from '@angular/material';
import { HttpClient } from '@angular/common/http';

import { VolunteerRequestService } from './volunteer-request.service';
import { SnackBarService } from '../../shared/snack-bar.service';
import { merge } from "rxjs/observable/merge";
import { GenericDataSource } from "../../shared/GenericDataSource";


@Component({
  selector: 'app-volunteer-request',
  templateUrl: './volunteer-request.component.html',
  styleUrls: [ './volunteer-request.component.scss' ],
  providers: [
    VolunteerRequestService,
  ]
})
export class VolunteerRequestComponent implements OnInit, AfterViewInit {
  VRData: GenericDataSource;
  columnsToDisplay = [ 'id', 'accepted', 'title', 'delete', 'editVr' ];
  totalElements = 0;

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  constructor(
    private _http: HttpClient,
    private _volunteerRequestService: VolunteerRequestService,
    private _sb: SnackBarService
  ) {
  }

  ngOnInit() {
    this.VRData = new GenericDataSource(this._volunteerRequestService);
    this.VRData.loadPage();

    this._volunteerRequestService.getPage().subscribe(d => {
      if (d && d.totalElements) {
        this.totalElements = d.totalElements;
      }
    });
  }

  ngAfterViewInit() {
    this.sort.sortChange.subscribe(() => this.paginator.pageIndex = 0);

    merge(this.sort.sortChange, this.paginator.page)
      .pipe(
        tap(() => this._loadVRPage())
      ).subscribe();
  }

  changeAccepted(id: number) {
    this._volunteerRequestService.accept(id).subscribe(
      () => {
        this._sb.open('Zapisano');
        this._loadVRPage();
      },
      () => this._sb.warning()
    );
  }

  deleteVr(id: number) {
    this._volunteerRequestService.delete(id).subscribe(
      () => {
        this._sb.open('Usunięto ogłoszenie');
        this._loadVRPage();
      },
      () => this._sb.warning()
    );
  }

  private _loadVRPage() {
    this.VRData.loadPage(
      this.paginator.pageIndex,
      this.paginator.pageSize,
      { name: this.sort.active, value: this.sort.direction }
    );
  }

}
