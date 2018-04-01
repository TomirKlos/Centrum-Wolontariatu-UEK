import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { LoginComponent } from './before-login/login/login.component';
import { AccountComponent } from './account.component';
import { SignupComponent } from './before-login/signup/signup.component';
import { ActivateComponent } from './before-login/activate/activate.component';
import { ResetPasswordInitComponent } from './before-login/reset-password-init/reset-password-init.component';
import { ResetPasswordFinishComponent } from './before-login/reset-password-finish/reset-password-finish.component';
import { AuthGuardService } from '../shared/auth/gurads/auth-guard.service';
import { ProfileComponent } from './after-login/profile/profile.component';

const routes: Routes = [
  {
    component: AccountComponent,
    path: '',
    children: [
      {
        path: 'login',
        component: LoginComponent,
      },
      {
        path: 'signup',
        component: SignupComponent,
      },
      {
        path: 'activate/:activation-key',
        component: ActivateComponent,
      },
      {
        path: 'reset-password',
        component: ResetPasswordInitComponent,
      },
      {
        path: 'reset-password/:reset-key',
        component: ResetPasswordFinishComponent,
      },
      {
        path: 'profile',
        component: ProfileComponent,
        canActivate: [AuthGuardService]
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AccountRoutingModule {
}
