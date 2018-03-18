import { Injectable } from '@angular/core';
import { LoginCredentials } from '../../shared/interfaces';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';

@Injectable()
export class RegisterService {

  constructor(private http: HttpClient) {
  }

  createAccount(loginCredentials: LoginCredentials) {
    return this.http.post(environment.apiEndpoint + 'register', loginCredentials, {observe: 'response'});
  }

}
