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

  loadVolunteerRequests(page = 0, size = 50) {
    this._vRService.getAll(page, size).subscribe(d => {
      this._vRSubject.next(d);
    });
  }

  loadOneVolunteerRequest(page = 0, size = 50, id:number) {
      this._vRService.getVolunteerRequestById(page, size, id).subscribe(d => {
      this._vRSubject.next(d);

    });
  }

  loadSpecializedVolunteerRequest(page = 0, size = 50, query) {
       this._vRService.getVolunteerRequestSpecialized(page, size, query).subscribe(d => {
       this._vRSubject.next(d);

     });
   }

}
