import { Injectable } from '@angular/core';
import { catchError } from 'rxjs/operators';
import { HttpClient } from '@angular/common/http';

import { VolunteerRequestVM, responseVolunteerRequestVM } from '../../../shared/interfaces';
import { SnackBarService } from '../../../shared/snack-bar.service';
import { ServerResourceService } from '../../../shared/server-resource.service';

@Injectable()
export class MyRequestsService extends ServerResourceService<responseVolunteerRequestVM> {

  constructor(_http: HttpClient, _snackBar: SnackBarService) {
    super(_http, _snackBar);
    this._url = this._url;
  }

  loadApplications(id: number) {
    return this._http.get(this._url + id).pipe(
      catchError(err => {
        this._snackBar.warning();
        return err;
      })
    );
  }

  getBadgeCount(id: number) {
    return this._http.get(this._url + "/responseVr/unseen" + id).pipe(
      catchError(err => {
        this._snackBar.warning();
        return err;
      })
    );
  }

  get(id: number){
    return this._http.get(this._url + "/responseVr/unseen?volunteerRequestId="+ id);
  }

  getIDs() {
    return this._http.get(this._url + "/vrequest/mineId").pipe(
      catchError(err => {
        this._snackBar.warning();
        return err;
      })
    );
  }

}
