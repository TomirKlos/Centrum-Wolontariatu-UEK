import { NgModule } from '@angular/core';

import { AccountRoutingModule } from './account-routing.module';
import { AccountComponent } from './account.component';
import { LoginComponent } from './before-login/login/login.component';
import { LayoutModule } from '../shared/layout.module';
import { SignupComponent } from './before-login/signup/signup.component';
import { ActivateComponent } from './before-login/activate/activate.component';
import { ResetPasswordInitComponent } from './before-login/reset-password-init/reset-password-init.component';
import { ResetPasswordFinishComponent } from './before-login/reset-password-finish/reset-password-finish.component';
import { ProfileComponent } from './after-login/profile/profile.component';
import { PasswordsFormGroupComponent } from './shared/passwords-form-group/passwords-form-group.component';

@NgModule({
  imports: [
    LayoutModule,
    AccountRoutingModule
  ],
  declarations: [
    AccountComponent,
    LoginComponent,
    SignupComponent,
    ActivateComponent,
    ResetPasswordInitComponent,
    ResetPasswordFinishComponent,
    ProfileComponent,
    PasswordsFormGroupComponent
  ]
})
export class AccountModule {
}
