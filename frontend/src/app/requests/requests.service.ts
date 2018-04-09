import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";

import { GenericService } from "../shared/generic.service";
import { VolunteerRequestVM } from "../shared/interfaces";

@Injectable()
export class RequestsService extends GenericService<VolunteerRequestVM> {

  constructor(_http: HttpClient) {
    super(_http);
    this._url = this._url + '/vrequest'
  }

}
