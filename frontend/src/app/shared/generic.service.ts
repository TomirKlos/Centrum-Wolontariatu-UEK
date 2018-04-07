import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { map } from 'rxjs/operators';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';

import { Page, Param } from "./interfaces";
import { environment } from "../../environments/environment";

@Injectable()
export abstract class GenericService<T> {
  protected _url = environment.apiEndpoint;
  private _pageSubject = new BehaviorSubject<Page<T>>(null);

  protected constructor(protected _http: HttpClient) {
  }

  getAll(page = 0, size = 50, ...otherParams: Param[]) {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    for (const param of otherParams) {
      if (param.name !== undefined && param.value !== undefined) {
        params = params.set(param.name, param.value);
      }
    }

    return this._http.get(this._url, { params: params }).pipe(
      map((page: Page<T>) => {
        this._pageSubject.next(page);
        return page.content;
      }))
  }

  getPage() {
    return this._pageSubject.asObservable();
  }

  delete(id: number) {
    return this._http.delete(this._url + '/' + id);
  }
}
