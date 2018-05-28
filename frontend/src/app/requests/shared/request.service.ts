import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { VolunteerRequestVM } from '../../shared/interfaces';
import { SnackBarService } from '../../shared/snack-bar.service';
import { ServerResourceService } from '../../shared/server-resource.service';

@Injectable()
export class RequestService extends ServerResourceService<VolunteerRequestVM> {

  constructor(_http: HttpClient, _snackBar: SnackBarService) {
    super(_http, _snackBar);
    this._url = this._url + '/vrequest';
  }

  public getGroups() {
    return this._http.get(this._url + "/category/");
  }

  public getMine() {
    return this._http.get(this._url + "/mine");
  }

}
