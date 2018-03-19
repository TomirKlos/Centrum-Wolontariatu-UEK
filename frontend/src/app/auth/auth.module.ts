import { NgModule } from '@angular/core';

import { AuthRoutingModule } from './auth-routing.module';
import { AuthComponent } from './auth.component';
import { LoginComponent } from './login/login.component';
import { LayoutModule } from '../shared/layout.module';
import { SignupComponent } from './signup/signup.component';
import { ActivateComponent } from './activate/activate.component';
import { ResetPasswordInitComponent } from './reset-password-init/reset-password-init.component';
import { ResetPasswordFinishComponent } from './reset-password-finish/reset-password-finish.component';

@NgModule({
  imports: [
    LayoutModule,
    AuthRoutingModule
  ],
  declarations: [
    AuthComponent,
    LoginComponent,
    SignupComponent,
    ActivateComponent,
    ResetPasswordInitComponent,
    ResetPasswordFinishComponent
  ]
})
export class AuthModule {
}
