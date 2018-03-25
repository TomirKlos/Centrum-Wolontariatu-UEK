import { Injectable } from '@angular/core';
import { Subject } from 'rxjs/Subject';
import { Observable } from 'rxjs/Observable';
import { JwtHelperService } from '@auth0/angular-jwt';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';

@Injectable()
export class UserService {
  constructor(private jwtHelper: JwtHelperService, private http: HttpClient) {
  }

  private _isLoggedInState = new Subject<boolean>();

  get isLoggedInState(): Observable<boolean> {
    return this._isLoggedInState.asObservable();
  }

  private _isLoggedIn = false;

  get isLoggedIn(): boolean {
    return this._isLoggedIn;
  }

  set isLoggedIn(value: boolean) {
    this._isLoggedInState.next(value);
    this._isLoggedIn = value;

    this._setRoles();
  }

  private _rolesState = new Subject<string[]>();

  get rolesState(): Observable<string[]> {
    return this._rolesState.asObservable();
  }

  private _roles = [];

  get roles(): string[] {
    return this._roles;
  }

  hasAnyRole(roles: string[]): boolean {
    for (const role of roles) {
      for (const _role of this._roles) {
        if (role === _role) {
          return true;
        }
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

  private _setRoles() {
    const jwtO = this.jwtHelper.decodeToken(this.getJwtToken());
    const roles = (jwtO && jwtO.auth ? jwtO.auth : ['GUEST']);

    this._roles = roles;
    this._rolesState.next(roles);
  }

  private getJwtToken() {
    return localStorage.getItem('jwtToken');
  }
}
