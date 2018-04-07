import { VolunteerRequest } from '../../shared/interfaces';
import { CollectionViewer, DataSource } from '@angular/cdk/collections';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { VolunteerRequestService } from './volunteer-request.service';
import { Observable } from 'rxjs/Observable';

export class VolunteerRequestDataSource implements DataSource<VolunteerRequest> {
  private _vRSubject = new BehaviorSubject<VolunteerRequest[]>([]);

  constructor(private _vRService: VolunteerRequestService) {
  }

  connect(collectionViewer: CollectionViewer): Observable<VolunteerRequest[]> {
    return this._vRSubject.asObservable();
  }

  disconnect() {
    this._vRSubject.complete();
  }

  loadVolunteerRequests(page = 0, size = 50, sort?: string) {
    this._vRService.getAll(page, size, sort).subscribe(d => {
      this._vRSubject.next(d);
    });
  }
}
