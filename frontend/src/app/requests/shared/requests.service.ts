import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { GenericService } from '../../shared/generic.service';
import { VolunteerRequestVM } from '../../shared/interfaces';
import { SnackBarService } from '../../shared/snack-bar.service';

@Injectable()
export class RequestsService extends GenericService<VolunteerRequestVM> {

  constructor(_http: HttpClient, _snackBar: SnackBarService) {
    super(_http, _snackBar);
    this._url = this._url + '/vrequest';
  }

}
