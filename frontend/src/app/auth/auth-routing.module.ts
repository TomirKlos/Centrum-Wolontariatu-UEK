import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { AuthComponent } from './auth.component';
import { RegisterComponent } from './register/register.component';

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
        path: 'register',
        component: RegisterComponent,
        data: {
          title: 'Rejestracja'
        }
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
