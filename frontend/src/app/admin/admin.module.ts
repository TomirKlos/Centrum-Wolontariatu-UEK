import { NgModule } from '@angular/core';

import { AdminRoutingModule } from './admin-routing.module';
import { AdminComponent } from './admin.component';
import { UsersComponent } from './users/users.component';
import { LayoutModule } from '../shared/layout.module';
import { VolunteerRequestComponent } from './volunteer-request/volunteer-request.component';
import { DialogComponent } from './volunteer-request/dialog/dialog.component';
import {MatAutocompleteModule} from '@angular/material';
import { MatDialogModule, MatDialog, MAT_DIALOG_SCROLL_STRATEGY, MAT_DIALOG_DATA } from '@angular/material';



@NgModule({
  imports: [
    LayoutModule,
    AdminRoutingModule,
    MatAutocompleteModule,
    MatDialogModule,
  ],
  declarations: [AdminComponent, UsersComponent, VolunteerRequestComponent, DialogComponent],
  providers: [
    MatDialog,
    { provide: MAT_DIALOG_DATA, useValue: [] },
  ]
})
export class AdminModule {
}
