import { Injectable } from '@angular/core';
import { catchError } from 'rxjs/operators';
import { HttpClient } from '@angular/common/http';

import { VolunteerRequestVM, VolunteerAdVM } from '../../../shared/interfaces';
import { SnackBarService } from '../../../shared/snack-bar.service';
import { ServerResourceService } from '../../../shared/server-resource.service';

@Injectable()
export class AdminAdsService extends ServerResourceService<VolunteerAdVM> {

  constructor(_http: HttpClient, _snackBar: SnackBarService) {
    super(_http, _snackBar);
    this._url = this._url + '/vAd';
  }

  accept(id: number) {
    return this._http.post(this._url + '/accept', { id }).pipe(
      catchError(err => {
        this._snackBar.warning();
        return err;
      })
    );
  }

}
