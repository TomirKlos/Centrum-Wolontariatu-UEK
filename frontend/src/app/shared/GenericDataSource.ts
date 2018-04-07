import { CollectionViewer, DataSource } from '@angular/cdk/collections';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { Observable } from 'rxjs/Observable';

import { Param } from "./interfaces";

export class GenericDataSource implements DataSource {
  private _data = new BehaviorSubject<any[]>([]);

  constructor(private _service) {
  }

  connect(collectionViewer: CollectionViewer): Observable<any[]> {
    return this._data.asObservable();
  }

  disconnect() {
    this._data.complete();
  }

  loadPage(page = 0, size = 50, ...otherParams: Param[]) {
    this._service.getAll(page, size, ...otherParams).subscribe(d => {
      this._data.next(d);
    });
  }
}
