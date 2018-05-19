import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { JWTToken, LoginCredentials } from '../interfaces';
import { Observable } from 'rxjs/Observable';
import { catchError, map } from 'rxjs/operators';
import { environment } from '../../../environments/environment';
import { Router } from '@angular/router';
import { SnackBarService } from '../snack-bar.service';
import { UserService } from './user.service';
import 'rxjs/add/observable/of';

@Injectable()
export class AuthService {
  redirectUrl = '/';
  private _jwtTokenName = 'jwtToken';

  constructor(private http: HttpClient, private router: Router, private sb: SnackBarService, private userService: UserService) {
  }

  login(loginCredentials: LoginCredentials): Observable<boolean> {
    return this.http.post(environment.apiEndpoint + '/authenticate', loginCredentials).pipe(
      map((jwtToken: JWTToken) => {
        this._saveToken(jwtToken);
        this.userService.isLoggedIn = true;
        return true;
      }),
      catchError((err) => Observable.of(err.status === 401 ? false : err)
      )
    );
  }

  logout(): void {
    localStorage.removeItem(this._jwtTokenName);
    this.router.navigateByUrl('/').then();
    this.userService.isLoggedIn = false;
    this.sb.open('Wylogowano', { duration: 2000 });
  }

  private _saveToken(token: JWTToken) {
    localStorage.setItem(this._jwtTokenName, token.jwtToken);
  }

}
