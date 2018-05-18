import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { catchError, map } from 'rxjs/operators';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';

import { Page, Param } from './interfaces';
import { environment } from '../../environments/environment';
import { SnackBarService } from './snack-bar.service';

@Injectable()
export abstract class GenericService<T> {
  protected _url = environment.apiEndpoint;
  private _pageSubject = new BehaviorSubject<Page<T>>(null);

  protected constructor(protected _http: HttpClient, protected _snackBar: SnackBarService) {
  }

  getAll(...httpParams: Param[]) {
    return this.getAllFromRelativePath('', ...httpParams);
  }

  getAllFromRelativePath(path: string, ...httpParams: Param[]) {
    let params = new HttpParams();

    for (const param of httpParams) {
      if (param.value !== undefined && !param.value.toString().includes('undefined')) {
        params = params.set(param.name, param.value.toString());
      }
    }

    return this._http.get(this._url + '/' + path, { params: params }).pipe(
      map((page: Page<T>) => {
        this._pageSubject.next(page);
        return page.content;
      }));
  }

  getOne(id: number) {
    return this._http.get(this._url + '/' + id);
  }

  getPage() {
    return this._pageSubject.asObservable();
  }

  delete(id: number) {
    return this._http.delete(this._url + '/' + id).pipe(
      map(value => {
        this._snackBar.open('Zapisano');
        return value;
      }),
      catchError(err => {
        this._snackBar.warning();
        return err;
      })
    );
  }
}
