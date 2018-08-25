import { NgModule } from '@angular/core';

import { LayoutModule } from '../shared/layout.module';
import { AdminRoutingModule } from './admin-routing.module';
import { AdminComponent } from './admin.component';
import { UsersComponent } from './users/users.component';
import { CategoriesComponent } from './categories/categories.component';
import { FormsModule } from '@angular/forms';

@NgModule({
  imports: [
    LayoutModule,
    AdminRoutingModule,
    FormsModule,
  ],
  declarations: [
    AdminComponent,
    UsersComponent,
    CategoriesComponent

  ],
  entryComponents: [
  ]
})
export class AdminModule {
}
