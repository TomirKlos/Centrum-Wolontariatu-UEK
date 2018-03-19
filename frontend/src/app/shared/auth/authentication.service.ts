import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { JWTToken, LoginCredentials } from '../interfaces';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/map';
import 'rxjs/add/observable/of';
import { catchError, map } from 'rxjs/operators';
import { environment } from '../../../environments/environment';

@Injectable()
export class AuthenticationService {
  private jwtTokenName = 'jwtToken';

  constructor(private http: HttpClient) {
  }

  login(loginCredentials: LoginCredentials): Observable<number> {
    return this.http.post(environment.apiEndpoint + 'authenticate', loginCredentials, { observe: 'response' }).pipe(
      map((res: HttpResponse<JWTToken>) => {
        this.setSession(res.body);
        return res.status;
      }),
      catchError((err: HttpErrorResponse) => Observable.of(err.status)),
    );
  }

  logout(): void {
    localStorage.removeItem(this.jwtTokenName);
  }

  private setSession(token: JWTToken) {
    localStorage.setItem(this.jwtTokenName, token.jwtToken);
  }
}
