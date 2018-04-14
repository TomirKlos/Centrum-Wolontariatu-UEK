import { NgModule } from '@angular/core';

import { LayoutModule } from '../shared/layout.module';
import { AdminRoutingModule } from './admin-routing.module';
import { AdminComponent } from './admin.component';
import { UsersComponent } from './users/users.component';
import { VolunteerRequestComponent } from './volunteer-request/volunteer-request.component';
import { ViewRequestDialogComponent } from "../requests/view-request/view-request-dialog.component";

@NgModule({
  imports: [
    LayoutModule,
    AdminRoutingModule,
  ],
  declarations: [
    AdminComponent,
    UsersComponent,
    VolunteerRequestComponent,
    ViewRequestDialogComponent
  ],
  entryComponents: [
    ViewRequestDialogComponent
  ]
})
export class AdminModule {
}
