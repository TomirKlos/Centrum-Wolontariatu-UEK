import { NgModule } from '@angular/core';

import { LayoutModule } from '../shared/layout.module';
import { AdminRoutingModule } from './admin-routing.module';
import { AdminComponent } from './admin.component';
import { UsersComponent } from './users/users.component';
import { CategoriesComponent } from './categories/categories.component';
import { FormsModule } from '@angular/forms';
import {CertificatesComponent} from './certificates/certificates.component';
import {BannerComponent} from './banner/banner.component';
import {BannerService} from './banner/banner.service';

@NgModule({
  imports: [
    LayoutModule,
    AdminRoutingModule,
    FormsModule,
  ],
  declarations: [
    AdminComponent,
    UsersComponent,
    CategoriesComponent,
    CertificatesComponent,
    BannerComponent
  ],
  providers: [
    BannerService
  ],
  entryComponents: [
  ]
})
export class AdminModule {
}
