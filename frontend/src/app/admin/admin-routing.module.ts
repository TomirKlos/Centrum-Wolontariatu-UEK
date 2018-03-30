import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AdminComponent } from './admin.component';
import { UsersComponent } from './users/users.component';
import { AdminGuardService } from '../shared/auth/admin-guard.service';
import { AuthGuardService } from '../shared/auth/auth-guard.service';
import { VolunteerRequestComponent } from './volunteer-request/volunteer-request.component';

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
        path: 'volunteerrequest',
        component: VolunteerRequestComponent
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AdminRoutingModule {
}
