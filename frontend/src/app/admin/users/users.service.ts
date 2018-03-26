import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { map } from 'rxjs/operators';
import { Pageable, User } from '../../shared/interfaces';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';

@Injectable()
export class UsersService {
  private _pageSubject = new BehaviorSubject<Pageable<User>>(null);
  private _url = environment.apiEndpoint + 'users';

  constructor(private _http: HttpClient) {
  }

  getAll(page = 0, size = 50) {
    return this._http.get(this._url, {
      params: new HttpParams()
        .set('page', page.toString())
        .set('size', size.toString())
    }).pipe(
      map((usersPage: Pageable<User>) => {
        this._pageSubject.next(usersPage);
        return usersPage.content;
      })
    );
  }

  getPage() {
    return this._pageSubject.asObservable();
  }

  delete(id: number) {
    return this._http.delete(this._url + '/' + id);
  }

  activate(id: number) {
    return this._http.post(this._url + '/activate', { id });
  }
}
