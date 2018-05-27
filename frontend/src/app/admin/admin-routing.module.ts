import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { AdminGuardService } from '../shared/auth/gurads/admin-guard.service';
import { AuthGuardService } from '../shared/auth/gurads/auth-guard.service';
import { AdminComponent } from './admin.component';
import { UsersComponent } from './users/users.component';
import { CategoriesComponent } from './categories/categories.component';

const routes: Routes = [
  {
    path: 'admin',
    component: AdminComponent,
    canActivate: [AuthGuardService, AdminGuardService],
    children: [
      {
        path: 'users',
        component: UsersComponent
      },
      {
        path: 'categories',
        component: CategoriesComponent
      },
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AdminRoutingModule {
}
