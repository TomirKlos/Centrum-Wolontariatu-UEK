import { Injectable } from '@angular/core';
import { Subject } from 'rxjs/Subject';

@Injectable()
export class LoadingBarService {
  private displaySource = new Subject<boolean>();
  display = this.displaySource.asObservable();

  constructor() {
  }

  turnOn() {
    this.displaySource.next(true);
  }

  turnOff() {
    this.displaySource.next(false);
  }
}

