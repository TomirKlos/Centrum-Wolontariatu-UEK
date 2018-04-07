import { NgModule } from '@angular/core';

import { LayoutModule } from '../shared/layout.module';
import { AdminRoutingModule } from './admin-routing.module';
import { AdminComponent } from './admin.component';
import { UsersComponent } from './users/users.component';
import { DialogComponent } from './volunteer-request/dialog/dialog.component';
import { VolunteerRequestComponent } from './volunteer-request/volunteer-request.component';

@NgModule({
  imports: [
    LayoutModule,
    AdminRoutingModule,
  ],
  declarations: [
    AdminComponent,
    UsersComponent,
    VolunteerRequestComponent,
    DialogComponent,
  ]
})
export class AdminModule {
}
