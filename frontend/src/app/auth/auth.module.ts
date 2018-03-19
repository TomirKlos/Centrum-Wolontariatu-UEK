import { NgModule } from '@angular/core';

import { AuthRoutingModule } from './auth-routing.module';
import { AuthComponent } from './auth.component';
import { LoginComponent } from './login/login.component';
import { LayoutModule } from '../shared/layout.module';
import { SignupComponent } from './signup/signup.component';
import { ActivateComponent } from './activate/activate.component';
import { PasswordResetInitComponent } from './password-reset-init/password-reset-init.component';
import { PasswordResetFinishComponent } from './password-reset-finish/password-reset-finish.component';

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
    PasswordResetInitComponent,
    PasswordResetFinishComponent
  ]
})
export class AuthModule {
}
