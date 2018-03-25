import { NgModule } from '@angular/core';
import { AuthGuardService } from './auth-guard.service';
import { AuthService } from './auth.service';
import { UserService } from './user.service';
import { environment } from '../../../environments/environment';
import { JwtModuleOptions } from '@auth0/angular-jwt';
import { AdminGuardService } from './admin-guard.service';

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
    whitelistedDomains: ['localhost:8080']
  }
};

function myTokenGetter() {
  return localStorage.getItem(environment.auth.tokenName);
}
