import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { environment } from '../../environments/environment';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/debounceTime';
import 'rxjs/add/operator/distinctUntilChanged';
import 'rxjs/add/operator/switchMap';

@Injectable()
export class SearchService {
  private _url = environment.apiEndpoint + '/vrequest/solr/';
  queryUrl: string = '';

  constructor(private _http: HttpClient) { }

  search(terms: Observable<string>) {
    return terms.debounceTime(400)
      .distinctUntilChanged()
      .switchMap(term => this.searchEntries(term));
  }

  searchEntries(term:string) {
    if(term.length>2){
      return this._http.get(this._url + term);
    }
    return this._http.get(this._url + "t");
  }
}
