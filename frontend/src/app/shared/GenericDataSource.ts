import { CollectionViewer, DataSource } from '@angular/cdk/collections';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { Observable } from 'rxjs/Observable';

import { Param } from './interfaces';

export class GenericDataSource<T> implements DataSource<T> {
  private _data = new BehaviorSubject<T[]>([]);

  constructor(private _service) {
  }

  connect(collectionViewer: CollectionViewer): Observable<T[]> {
    return this._data.asObservable();
  }

  disconnect() {
    this._data.complete();
  }

  loadPage(...httpParams: Param[]) {
    this._service.getAll(...httpParams).subscribe(d => {
      this._data.next(d);
    });
  }

  nextData(data: any) {
    this._data.next(data);
  }
}
