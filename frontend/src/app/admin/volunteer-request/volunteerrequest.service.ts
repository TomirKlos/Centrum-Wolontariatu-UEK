import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { map } from 'rxjs/operators';
import { Pageable, VolunteerRequest } from '../../shared/interfaces';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';


@Injectable()
export class VolunteerRequestService {
  private _pageSubject = new BehaviorSubject<Pageable<VolunteerRequest>>(null);
  private _url = environment.apiEndpoint + 'vrequest/';
  private _urlOneVolunteerRequest = environment.apiEndpoint + 'vrequest/?search=id==';
  private _urlVolunteerRequestQuery = environment.apiEndpoint + 'vrequest/';
  //+ 'vrequest/?search=accepted=in=';

  constructor(private _http: HttpClient) {
  }

  getAll(page = 0, size = 50) {
    return this._http.get(this._url, {
      params: new HttpParams()
        .set('page', page.toString())
        .set('size', size.toString())
    }).pipe(
      map((usersPage: Pageable<VolunteerRequest>) => {
        this._pageSubject.next(usersPage);
        return usersPage.content;
      })
    );
  } 

  getPage() {
    return this._pageSubject.asObservable();
  }
/*
  getVolunteerRequestById(page=0, size=1, id) {
    return this._http.get(this._urlOneVolunteerRequest+id, {
      params: new HttpParams()
        .set('page', page.toString())
        .set('size', size.toString())
    }).pipe(
      map((usersPage: Pageable<VolunteerRequest>) => {
        this._pageSubject.next(usersPage);
        return usersPage.content;
      })
    );
  } */

  getVolunteerRequestById(page=0, size=1, id) {
    return this._http.get(this._urlOneVolunteerRequest+id, {
      params: new HttpParams()
        .set('page', page.toString())
        .set('size', size.toString())
    }).pipe(
      map((usersPage: Pageable<VolunteerRequest>) => {
        this._pageSubject.next(usersPage);
        return usersPage.content;
      })
    );  
  }

  getVolunteerRequestSpecialized(page=0, size=1, query) {
    return this._http.get(this._urlVolunteerRequestQuery+query, {
      params: new HttpParams()
        .set('page', page.toString())
        .set('size', size.toString())
    }).pipe(
      map((usersPage: Pageable<VolunteerRequest>) => {
        this._pageSubject.next(usersPage);
        return usersPage.content;
      })
    );  
  }

  delete(id: number) {
    return this._http.delete(this._url + id);
  }

  accept(id: number) {
    return this._http.post(this._url + 'accept', { id });
  }
  

}
