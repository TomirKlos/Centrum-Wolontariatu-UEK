import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { VolunteerAdVM } from '../../shared/interfaces';
import { SnackBarService } from '../../shared/snack-bar.service';
import { ServerResourceService } from '../../shared/server-resource.service';

@Injectable()
export class AdService extends ServerResourceService<VolunteerAdVM> {

  constructor(_http: HttpClient, _snackBar: SnackBarService) {
    super(_http, _snackBar);
    this._url = this._url + '/vAd';
  }


}
