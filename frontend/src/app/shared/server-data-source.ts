import { CollectionViewer, DataSource } from '@angular/cdk/collections';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { Observable } from 'rxjs/Observable';
import { MatPaginator, MatSort } from '@angular/material';
import { tap } from 'rxjs/operators';
import { merge } from 'rxjs/observable/merge';

import { ServerResourceService } from './server-resource.service';

export class ServerDataSource<T> implements DataSource<T> {
  relativePathToServerResource = '';
  private _data = new BehaviorSubject<T[]>([]);

  constructor(
    private _serverResourceService: ServerResourceService<T>,
    private _paginator: MatPaginator,
    private _sort: MatSort
  ) {
  }

  private _totalElements = 0;

  get totalElements(): number {
    return this._totalElements;
  }

  connect(collectionViewer: CollectionViewer): Observable<T[]> {
    return this._data.asObservable();
  }

  disconnect() {
    this._data.complete();
  }

  initAfterViewInit() {
    this._sort.sortChange.subscribe(() => this._paginator.pageIndex = 0);

    merge(this._sort.sortChange, this._paginator.page)
      .pipe(
        tap(() => this.loadPage())
      ).subscribe();
  }

  loadPage() {
    this._serverResourceService.getPageFromRelativePath(this.relativePathToServerResource,
      { name: 'page', value: this._paginator.pageIndex },
      { name: 'size', value: this._paginator.pageSize },
      { name: 'sort', value: this._sort.active + ',' + this._sort.direction }
    ).subscribe(d => this._data.next(d.content));
  }

  loadSimplePage() {
    this._serverResourceService.getPageFromRelativePath(this.relativePathToServerResource,
      { name: 'page', value: 0 },
      { name: 'size', value: 10 },
      { name: 'sort', value: "" }
    ).subscribe(d => this._data.next(d.content));
  }
}
