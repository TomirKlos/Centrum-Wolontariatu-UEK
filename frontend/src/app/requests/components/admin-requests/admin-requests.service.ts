import { Injectable } from '@angular/core';
import { VolunteerRequestVM } from '../../../shared/interfaces';
import { HttpClient } from '@angular/common/http';
import { GenericService } from '../../../shared/generic.service';
import { SnackBarService } from '../../../shared/snack-bar.service';
import { catchError } from 'rxjs/operators';

@Injectable()
export class AdminRequestsService extends GenericService<VolunteerRequestVM> {

  constructor(_http: HttpClient, _snackBar: SnackBarService) {
    super(_http, _snackBar);
    this._url = this._url + '/vrequest';
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
