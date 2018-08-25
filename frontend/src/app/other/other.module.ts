import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { OtherRoutingModule } from './other-routing.module';
import { SidenavComponent } from './sidenav/sidenav.component';

@NgModule({
  imports: [
    CommonModule,
    OtherRoutingModule
  ],
  declarations: [
    SidenavComponent
  ]
})
export class OtherModule {
}
