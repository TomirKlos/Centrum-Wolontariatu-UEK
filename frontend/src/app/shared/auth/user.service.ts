import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { JwtHelperService } from '@auth0/angular-jwt';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';

@Injectable()
export class UserService {
  private readonly _CONST_GUEST = 'guest';
  private readonly _CONST_IS_LOGGED_IN = 'isLoggedIn';

  private _isLoggedIn = new BehaviorSubject<boolean>(false);
  private _roles$ = new BehaviorSubject<string[]>([this._CONST_GUEST]);

  constructor(private jwtHelper: JwtHelperService, private http: HttpClient) {
  }

  set isLoggedIn(value: boolean) {
    this._isLoggedIn.next(value);
    this._refreshRoles();
  }

  get isLoggedIn(): boolean {
    return this._isLoggedIn.getValue();
  }

  get isLoggedIn$() {
    return this._isLoggedIn.asObservable();
  }

  get roles$(): Observable<string[]> {
    return this._roles$.asObservable();
  }

  hasAnyRole(roles: string[]): boolean {
    const userRoles = this._roles$.getValue();

    for (const role of roles) {
      if (userRoles.find(value => value === role)) {
        return true;
      }
    }
    return false;
  }

  checkIfLoggedIn() {
    const rawToken = this.getJwtToken();
    if (!rawToken || this.jwtHelper.isTokenExpired(rawToken)) {
      this.isLoggedIn = false;
      return;
    }

    this.isLoggedIn = true;

    this.http.get(environment.apiEndpoint + 'account').subscribe(
      d => {
      },
      () => this.isLoggedIn = false,
    );
  }

  private _refreshRoles() {
    const jwtO = this.jwtHelper.decodeToken(this.getJwtToken());
    let tempRoles: string[];

    if (jwtO && jwtO.auth) {
      tempRoles = jwtO.auth;
      tempRoles.push(this._CONST_IS_LOGGED_IN);
    } else {
      tempRoles = [this._CONST_GUEST];
    }

    this._roles$.next(tempRoles);
  }

  private getJwtToken() {
    return localStorage.getItem('jwtToken');
  }
}
