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
  private _urlOneVolunteerRequest =  '?search=id==';
  private _urlVolunteerRequestQuery = '';

  private _urlAd = environment.apiEndpoint + 'vAd/';
  private _urlVr = environment.apiEndpoint + 'vrequest/';

  constructor(private _http: HttpClient) {
  }

  switchDisplayType(type){
    if(type==false){this._url=this._urlVr}
    if(type==true){this._url=this._urlAd}
  }
  
  getAll(page, size) {
    return this._http.get(this._url, {
      params: new HttpParams()
        .set('page', page.toString())
        .set('size', size.toString())
    }).pipe(
      map((vRPage: Pageable<VolunteerRequest>) => {
        this._pageSubject.next(vRPage);
        return vRPage.content;
      })
    );
  }

  getPage() {
    return this._pageSubject.asObservable();
  }

  getVolunteerRequestById(page = 0, size = 1, id) {
    return this._http.get(this._url + this._urlOneVolunteerRequest + id, {
      params: new HttpParams()
        .set('page', page.toString())
        .set('size', size.toString())
    }).pipe(
      map((vRPage: Pageable<VolunteerRequest>) => {
        this._pageSubject.next(vRPage);
        return vRPage.content;
      })
    );
  }

  getVolunteerRequestSpecialized(page = 0, size = 1, query) {
    return this._http.get(this._url + this._urlVolunteerRequestQuery + query, {
      params: new HttpParams()
        .set('page', page.toString())
        .set('size', size.toString())
    }).pipe(
      map((vRPage: Pageable<VolunteerRequest>) => {
        this._pageSubject.next(vRPage);
        return vRPage.content;
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
