import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

import { GenericService } from '../../shared/generic.service';
import { User } from '../../shared/interfaces';
import { SnackBarService } from '../../shared/snack-bar.service';

@Injectable()
export class UsersService extends GenericService<User> {

  constructor(_http: HttpClient, _snackBar: SnackBarService) {
    super(_http, _snackBar);
    this._url = this._url + '/users';
  }

  activate(id: number) {
    return this._http.post(this._url + '/activate', { id });
  }
}
