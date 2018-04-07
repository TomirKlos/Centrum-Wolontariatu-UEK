import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { map } from 'rxjs/operators';
import { Page, VolunteerRequest } from '../../shared/interfaces';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';


@Injectable()
export class VolunteerRequestService {
  private _pageSubject = new BehaviorSubject<Page<VolunteerRequest>>(null);
  private _url = environment.apiEndpoint + 'vrequest';

  constructor(private _http: HttpClient) {
  }

  getAll(page = 0, size = 50, sort?) {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    if (sort) {
      console.log(sort)
      params = params.set('sort', sort);
    }

    return this._http.get(this._url, { params: params }).pipe(
      map((page: Page<VolunteerRequest>) => {
        this._pageSubject.next(page);
        return page.content;
      }))
  }

  getPage() {
    return this._pageSubject.asObservable();
  }

  delete(id: number) {
    return this._http.delete(this._url + id);
  }

  accept(id: number) {
    return this._http.post(this._url + '/accept', { id });
  }

}
