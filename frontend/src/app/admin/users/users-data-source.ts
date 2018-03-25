import { User } from '../../shared/interfaces';
import { CollectionViewer, DataSource } from '@angular/cdk/collections';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { UsersService } from './users.service';
import { Observable } from 'rxjs/Observable';

export class UsersDataSource implements DataSource<User> {
  private _usersSubject = new BehaviorSubject<User[]>([]);

  constructor(private _usersService: UsersService) {
  }

  connect(collectionViewer: CollectionViewer): Observable<User[]> {
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

}
