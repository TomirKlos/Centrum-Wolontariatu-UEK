import { VolunteerRequestVM } from '../../shared/interfaces';
import { CollectionViewer, DataSource } from '@angular/cdk/collections';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { VolunteerRequestService } from './volunteer-request.service';
import { Observable } from 'rxjs/Observable';

export class VolunteerRequestDataSource implements DataSource<VolunteerRequestVM> {
  private _vRSubject = new BehaviorSubject<VolunteerRequestVM[]>([]);

  constructor(private _vRService: VolunteerRequestService) {
  }

  connect(collectionViewer: CollectionViewer): Observable<VolunteerRequestVM[]> {
    return this._vRSubject.asObservable();
  }

  disconnect() {
    this._vRSubject.complete();
  }

  loadVolunteerRequests(page = 0, size = 10) {
    this._vRService.getAll(page, size).subscribe(d => {
      this._vRSubject.next(d);
    });
  }

  loadOneVolunteerRequest(page = 0, size = 10, id:number) {
      this._vRService.getVolunteerRequestById(page, size, id).subscribe(d => {
      this._vRSubject.next(d);

    });
  }

  loadSpecializedVolunteerRequest(page = 0, size = 10, query) {
       this._vRService.getVolunteerRequestSpecialized(page, size, query).subscribe(d => {
       this._vRSubject.next(d);

     });
   }

}
