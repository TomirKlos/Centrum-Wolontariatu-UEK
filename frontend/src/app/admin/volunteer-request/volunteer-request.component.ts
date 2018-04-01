import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { VolunteerRequestService } from './volunteer-request.service';
import { MatPaginator } from '@angular/material';
import { HttpClient } from '@angular/common/http';
import { SnackBarService } from '../../shared/snack-bar.service';
import { VolunteerRequestDataSource } from './volunteer-request-data-source';
import { tap } from 'rxjs/operators';
import { DialogService } from '../../shared/dialog.service';
import { MatDialog, MatAutocompleteModule } from '@angular/material';
import { DialogComponent } from './dialog/dialog.component';
import { VolunteerRequest } from '../../../../src/app/shared/interfaces';
import { SearchService } from '../../shared/search-service.service'
import { Subject } from 'rxjs/Subject';
import { Observable } from 'rxjs/Observable';
import { FormControl } from '@angular/forms';


@Component({
  selector: 'app-volunteer-request',
  templateUrl: './volunteer-request.component.html',
  styleUrls: [ './volunteer-request.component.scss' ],
  providers: [
    VolunteerRequestService,
    SearchService
  ]
})
export class VolunteerRequestComponent implements OnInit, AfterViewInit {
  VRData: VolunteerRequestDataSource;
  columnsToDisplay = [ 'id', 'activated', 'tytuł', 'delete', 'editVr' ];
  totalElements = 0;
  dialogResult = "";
  onlyActivated = false;
  onlyNotActivated = false;
  activatedVrString = "?search=accepted=in=";

  results: Object;
  searchTerm$ = new Subject<string>();

  volunteerRequest: VolunteerRequest;

  @ViewChild(MatPaginator) paginator: MatPaginator;

  constructor(private _http: HttpClient, private _volunteerRequestService: VolunteerRequestService, private _sb: SnackBarService, private _dialog: MatDialog, private searchService: SearchService) {
    this.searchService.search(this.searchTerm$)
      .subscribe(results => {
        this.results = results;
      });
  }

  ngOnInit() {
    this.VRData = new VolunteerRequestDataSource(this._volunteerRequestService);
    this.VRData.loadVolunteerRequests();
    this._volunteerRequestService.getPage().subscribe(d => {
      if (d && d.totalElements) {
        this.totalElements = d.totalElements;
      }
    });
  }

  private _loadVRPage() {
    this.VRData.loadVolunteerRequests(
      this.paginator.pageIndex,
      this.paginator.pageSize
    );
  }

  private _loadVolunteerRequest(id: number) {
    this._volunteerRequestService.getVolunteerRequestById(0, 1, id).subscribe(data => {
      this.volunteerRequest = data[ 0 ];
    });
  }

  private selectSpecifiedVr(bool: boolean) {
    let query = this.activatedVrString;
    if (!bool) {
      if (!this.onlyNotActivated) {
        query = query + 0;
      } else {
        query = "";
      }
      this.onlyNotActivated = !this.onlyNotActivated
      if (this.onlyActivated) this.onlyActivated = !this.onlyActivated;
    } else {
      if (!this.onlyActivated) {
        query = query + 1;
      }
      else {
        query = "";
      }
      this.onlyActivated = !this.onlyActivated;
      if (this.onlyNotActivated) this.onlyNotActivated = !this.onlyNotActivated;
    }
    this.VRData.loadSpecializedVolunteerRequest(0, 10, query);
  }

  openDialog(id: number) {
    this._volunteerRequestService.getVolunteerRequestById(0, 1, id).subscribe(data => {
      this.volunteerRequest = data[ 0 ];
      this.openDialogHelper(id);
    });
  }

  openDialogHelper(id: number) {
    const dialogRef = this._dialog.open(DialogComponent, {
      height: '650px',
      width: '550px',
      data: this.volunteerRequest
    });
    dialogRef.afterClosed().subscribe(result => {
      this.dialogResult = result;
      if (result == "Confirm") {
        this.activateVr(id);
      }
    });
  }

  ngAfterViewInit() {
    this.paginator.page
      .pipe(
        tap(() => this._loadVRPage())
      )
      .subscribe();
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

  activateVr(id: number) {
    this._volunteerRequestService.accept(id).subscribe(
      () => {
        this._sb.open('Zatwierdzono ogłoszenie: ' + id);
        this._loadVRPage();
      },
      () => this._sb.warning()
    );
  }

}
