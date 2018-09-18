import { Injectable } from '@angular/core';
import { catchError } from 'rxjs/operators';
import { HttpClient } from '@angular/common/http';

import { VolunteerRequestVM, responseVolunteerRequestVM } from '../../../shared/interfaces';
import { SnackBarService } from '../../../shared/snack-bar.service';
import { ServerResourceService } from '../../../shared/server-resource.service';

//TODO add responseAd


@Injectable()                                           //responseVolunteerRequestVM
export class MyAdsService extends ServerResourceService<responseVolunteerRequestVM> {

  constructor(_http: HttpClient, _snackBar: SnackBarService) {
    super(_http, _snackBar);
  }

  loadApplications(id: number) {
    return this._http.get(this._url + '/vrequest/' + id).pipe(
      catchError(err => {
        this._snackBar.warning();
        return err;
      })
    );
  }

  getBadgeCount(id: number) {
    return this._http.get(this._url + "/vrequest/unseen/" + id).pipe(
      catchError(err => {
        this._snackBar.warning();
        return err;
      })
    );
  }
  /*get(id: number){
    return this._http.get(this._url + "/unseen"+ id);
  } */

  get(id: number){
    return this._http.get(this._url + "/vrequest/invite/unseen?adId="+ id);
  }

  getIDs() {
    return this._http.get(this._url + "/vAd/mineId").pipe(
      catchError(err => {
        this._snackBar.warning();
        return err;
      })
    );
  }

  expire(id: number) {
    return this._http.post(this._url + '/vAd/expire', { id });
  }

}
