import { Injectable } from '@angular/core';
import { catchError, map } from 'rxjs/operators';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { SnackBarService } from './snack-bar.service';
import { Page, Param } from './interfaces';
import { environment } from '../../environments/environment';

@Injectable()
export abstract class ServerResourceService<T> {
  protected _url = environment.apiEndpoint;

  protected constructor(protected _http: HttpClient, protected _snackBar: SnackBarService) {
  }

  getPageFromRelativePath(path: string, ...httpParams: Param[]): Observable<Page<T>> {
    let params = new HttpParams();

    // not includes params with undefined even in string
    for (const param of httpParams) {
      if (param.value !== undefined && !param.value.toString().includes('undefined')) {
        params = params.set(param.name, param.value.toString());
      }
    }
    return this._http.get(this._url + '/' + path, { params: params }) as Observable<Page<T>>;
  }

  getPage(...httpParams: Param[]): Observable<Page<T>> {
    return this.getPageFromRelativePath('', ...httpParams);
  }

  getPageWithUrl(path: string, ...httpParams: Param[]): Observable<Page<T>> {
    return this.getPageFromRelativePath(path, ...httpParams);
  }

  getOne(id: number): Observable<T> {
    return this._http.get(this._url + '/' + id) as Observable<T>;
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

  filterResultsBySearch(terms: Observable<string>, page: number, size: number): Observable<Page<T>> {
    return terms.debounceTime(400)
      .distinctUntilChanged()
      .switchMap(term => this.searchEntries(term, page, size)) as Observable<Page<T>>;
  }

  searchEntries(term: string, page: number, size: number) {
    if(term.length>2){
      return this._http.get(this._url + "/solrPage/" + term + "?page=" + page + "&size=" + size);
    }
  }
}
