import { NgModule } from '@angular/core';
import { JwtModuleOptions } from '@auth0/angular-jwt';

import { environment } from '../../../environments/environment';
import { AuthService } from './auth.service';
import { AdminGuardService } from './gurads/admin-guard.service';
import { AuthGuardService } from './gurads/auth-guard.service';
import { UserService } from './user.service';

@NgModule({
  providers: [
    AuthService,
    AuthGuardService,
    AdminGuardService,
    UserService
  ],
  declarations: []
})
export class AuthModule {
}

export const jwtModuleOptions: JwtModuleOptions = {
  config: {
    tokenGetter: myTokenGetter,
    whitelistedDomains: [ 'localhost:8080' ]
  }
};

export function myTokenGetter() {
  return localStorage.getItem(environment.tokenName);
}
