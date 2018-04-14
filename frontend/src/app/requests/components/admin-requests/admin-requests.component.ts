import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator, MatSort } from '@angular/material';
import { merge } from 'rxjs/observable/merge';
import { tap } from 'rxjs/operators';

import { VolunteerRequestVM } from '../../../shared/interfaces';
import { GenericDataSource } from '../../../shared/GenericDataSource';

import { AdminRequestsService } from './admin-requests.service';
import { RequestDialogService } from '../../shared/request-dialog.service';

@Component({
  selector: 'app-admin-requests',
  templateUrl: './admin-requests.component.html',
  providers: [ AdminRequestsService ]
})
export class AdminRequestsComponent implements OnInit, AfterViewInit {
  VRData: GenericDataSource<VolunteerRequestVM>;
  columnsToDisplay = [ 'id', 'accepted', 'title', 'delete', 'editVr' ];
  totalElements = 0;

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  constructor(
    private _adminRequestService: AdminRequestsService,
    private _dialogService: RequestDialogService
  ) {
  }

  ngOnInit() {
    this.VRData = new GenericDataSource(this._adminRequestService);
    this._loadVRPage();

    this._adminRequestService.getPage().subscribe(d => {
      if (d && d.totalElements) {
        this.totalElements = d.totalElements;
      }
    });
  }

  ngAfterViewInit() {
    this.sort.sortChange.subscribe(() => this.paginator.pageIndex = 0);

    merge(this.sort.sortChange, this.paginator.page)
      .pipe(tap(() => this._loadVRPage())).subscribe();
  }

  changeAccepted(id: number) {
    this._adminRequestService.accept(id).subscribe(() => this._loadVRPage());
  }

  deleteVr(id: number) {
    this._adminRequestService.delete(id).subscribe(() => this._loadVRPage());
  }

  openDialog(request: VolunteerRequestVM): void {
    this._dialogService.open(request);
  }

  private _loadVRPage() {
    this.VRData.loadPage(
      { name: 'page', value: this.paginator.pageIndex },
      { name: 'size', value: this.paginator.pageSize },
      { name: 'sort', value: this.sort.active + ',' + this.sort.direction }
    );
  }

}
