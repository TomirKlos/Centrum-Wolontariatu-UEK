import { CollectionViewer, DataSource } from '@angular/cdk/collections';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { Observable } from 'rxjs/Observable';
import { MatPaginator, MatSort } from '@angular/material';
import { tap } from 'rxjs/operators';
import { merge } from 'rxjs/observable/merge';
import { Subject } from 'rxjs/Subject';

import { ServerResourceService } from './server-resource.service';

export class ServerDataSource<T> implements DataSource<T> {
  relativePathToServerResource = '';
  private _data = new BehaviorSubject<T[]>([]);
  private _total = new BehaviorSubject<number>(0);

  private textSearch = new Subject<string>();

  constructor(
    private _serverResourceService: ServerResourceService<T>,
    private _paginator: MatPaginator,
    private _sort: MatSort,
    private dataType: string
  ) {
  }

  private _totalElements = 0;

  get totalElements(): number {
    return this._totalElements;
  }

  connect(collectionViewer: CollectionViewer): Observable<T[]> {
    return this._data.asObservable();
  }

  connectToSource(): Observable<T[]> {
    return this._data.asObservable();
  }
  connectToSourceElementsNumber(): Observable<number> {
    return this._total.asObservable();
  }

  disconnect() {
    this._data.complete();
  }

  initAfterViewInit() {
    this._sort.sortChange.subscribe(() => this._paginator.pageIndex = 0);

    if(this.dataType === "volunteerRequest" || this.dataType === "volunteerAd"){
      merge(this._sort.sortChange, this._paginator.page)
      .pipe(
        tap(() => this.loadPage())
      ).subscribe();
    }
    if(this.dataType=="volunteerRequestAcceptedOnly"){
      merge(this._sort.sortChange, this._paginator.page)
      .pipe(
        tap(() => this.loadAcceptedVrPage())
      ).subscribe();
    }
  }

  loadPage() {
    this._serverResourceService.getPageFromRelativePath(this.relativePathToServerResource,
      { name: 'page', value: this._paginator.pageIndex },
      { name: 'size', value: this._paginator.pageSize },
      { name: 'sort', value: this._sort.active + ',' + this._sort.direction }
    ).subscribe(d => {
      this._data.next(d.content);
      this._total.next(d.totalElements);
    });
  }

  loadAcceptedVrPage() {
    this._serverResourceService.getPageFromRelativePath(this.relativePathToServerResource,
      { name: 'search', value: "accepted=in=1" },
      { name: 'page', value: this._paginator.pageIndex },
      { name: 'size', value: this._paginator.pageSize },
      { name: 'sort', value: "id,desc" }
    ).subscribe(d => {
      this._data.next(d.content);
      this._total.next(d.totalElements);
    });
  }

  loadAcceptedVrPageWithCategories(queryPath: string) {
    this._serverResourceService.getPageFromRelativePath(this.relativePathToServerResource + 'v2/?' + queryPath,
      { name: 'page', value: this._paginator.pageIndex },
      { name: 'size', value: this._paginator.pageSize },
      { name: 'sort', value: "id,desc" }
    ).subscribe(d => {
      this._data.next(d.content);
      this._total.next(d.totalElements);
    });
  }

  loadApplicationPage(id: number) {
    this._serverResourceService.getPage(
      { name: 'volunteerRequestId', value: id },
      { name: 'page', value: this._paginator.pageIndex },
      { name: 'size', value: this._paginator.pageSize }
    ).subscribe(d => {
      this._data.next(d.content);
      this._total.next(d.totalElements);
    });
  }

  loadInvitationPage(id: number) {
    this._serverResourceService.getPage(
      { name: 'adId', value: id },
      { name: 'page', value: this._paginator.pageIndex },
      { name: 'size', value: this._paginator.pageSize }
    ).subscribe(d => {
      this._data.next(d.content);
      this._total.next(d.totalElements);
    });
  }

  generateFilteredSearchPage(textSearch: Subject<string>){
    this.textSearch = textSearch;
    return this.loadFilteredBySearchPage();
  }

  loadFilteredBySearchPage() {
    this._serverResourceService.filterResultsBySearch(this.textSearch, this._paginator.pageIndex, this._paginator.pageSize)
    .subscribe(d => {
      this._data.next(d.content);
      this._total.next(d.totalElements);
    });
  }

}
