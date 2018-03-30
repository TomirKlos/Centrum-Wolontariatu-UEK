import { VolunteerRequest } from '../../shared/interfaces';
import { CollectionViewer, DataSource } from '@angular/cdk/collections';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { VolunteerRequestService } from './volunteerrequest.service';
import { Observable } from 'rxjs/Observable';

export class UsersDataSource2 implements DataSource<VolunteerRequest> {
  private _usersSubject = new BehaviorSubject<VolunteerRequest[]>([]);

  constructor(private _usersService: VolunteerRequestService) {
  }

  connect(collectionViewer: CollectionViewer): Observable<VolunteerRequest[]> {
    return this._usersSubject.asObservable();
  }

  disconnect() {
    this._usersSubject.complete();
  }

  loadUsers(page = 0, size = 50) {
    this._usersService.getAll(page, size).subscribe(d => {
      this._usersSubject.next(d);
    });
  }

  loadOneVolunteerRequest(page = 0, size = 50, id:number) {
   // this._usersService.getVolunteerRequestById(page, size, id).subscribe(d => {
    //  this._usersSubject.next(d);
      this._usersService.getVolunteerRequestById(page, size, id).subscribe(d => {
      this._usersSubject.next(d);
      
    });
  } 

  loadSpecializedVolunteerRequest(page = 0, size = 50, query) {
       this._usersService.getVolunteerRequestSpecialized(page, size, query).subscribe(d => {
       this._usersSubject.next(d);
       
     });
   }

}