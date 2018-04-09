import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

import { GenericService } from "../../shared/generic.service";
import { User } from "../../shared/interfaces";

@Injectable()
export class UsersService extends GenericService<User> {

  constructor(_http: HttpClient) {
    super(_http);
    this._url = this._url + '/users'
  }

  activate(id: number) {
    return this._http.post(this._url + '/activate', { id });
  }
}
