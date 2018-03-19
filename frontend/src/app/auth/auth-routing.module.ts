import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { AuthComponent } from './auth.component';
import { SignupComponent } from './signup/signup.component';
import { ActivateComponent } from './activate/activate.component';
import { ResetPasswordInitComponent } from './reset-password-init/reset-password-init.component';
import { ResetPasswordFinishComponent } from './reset-password-finish/reset-password-finish.component';

const routes: Routes = [
  {
    component: AuthComponent,
    path: 'auth',
    children: [
      {
        path: 'login',
        component: LoginComponent,
        data: {
          title: 'Logowanie'
        }
      },
      {
        path: 'signup',
        component: SignupComponent,
        data: {
          title: 'Rejestracja'
        }
      },
      {
        path: 'activate/:activation-key',
        component: ActivateComponent,
        data: {
          title: 'Aktywacja konta'
        }
      },
      {
        path: 'reset-password',
        component: ResetPasswordInitComponent,
        data: {
          title: 'Reset hasła'
        }
      },
      {
        path: 'reset-password/:reset-key',
        component: ResetPasswordFinishComponent,
        data: {
          title: 'Wprowadź nowe hasło'
        }
      },
      {
        path: '**',
        redirectTo: '/',
        pathMatch: 'full'
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AuthRoutingModule {
}
