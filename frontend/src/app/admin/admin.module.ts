import { NgModule } from '@angular/core';

import { LayoutModule } from '../shared/layout.module';
import { AdminRoutingModule } from './admin-routing.module';
import { AdminComponent } from './admin.component';
import { UsersComponent } from './users/users.component';

@NgModule({
  imports: [
    LayoutModule,
    AdminRoutingModule,
  ],
  declarations: [
    AdminComponent,
    UsersComponent,

  ],
  entryComponents: [
  ]
})
export class AdminModule {
}
