import { NgModule } from '@angular/core';

import { AdminRoutingModule } from './admin-routing.module';
import { AdminComponent } from './admin.component';
import { UsersComponent } from './users/users.component';
import { LayoutModule } from '../shared/layout.module';

@NgModule({
  imports: [
    LayoutModule,
    AdminRoutingModule
  ],
  declarations: [AdminComponent, UsersComponent],
  providers: []
})
export class AdminModule {
}
