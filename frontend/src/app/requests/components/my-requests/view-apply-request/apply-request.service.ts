import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { catchError } from 'rxjs/operators';

import { SnackBarService } from '../../../../shared/snack-bar.service';
import { ServerResourceService } from '../../../../shared/server-resource.service';
import { responseVolunteerRequestVM } from '../../../../shared/interfaces';

@Injectable()
export class ApplyService extends ServerResourceService<responseVolunteerRequestVM> {

  constructor(_http: HttpClient, _snackBar: SnackBarService) {
    super(_http, _snackBar);
    this._url = this._url + '/responseVr';
  }

  accept(id: number) {
    return this._http.post(this._url + '/accept', { id }).pipe(
      catchError(err => {
        this._snackBar.warning();
        return err;
      })
    );
  }

  disableAccept(id: number) {
    return this._http.post(this._url + '/disableAccepted', { id }).pipe(
      catchError(err => {
        this._snackBar.warning();
        return err;
      })
    );
  }

  confirm(id: number, feedback: string) {
    return this._http.post(this._url + '/confirm', { id, feedback }).pipe(
      catchError(err => {
        this._snackBar.warning();
        return err;
      })
    );
  }

}
