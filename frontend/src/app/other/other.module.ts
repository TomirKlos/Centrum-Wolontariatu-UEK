import { NgModule} from '@angular/core';
import { CommonModule } from '@angular/common';

import { OtherRoutingModule } from './other-routing.module';
import { SidenavComponent } from './sidenav/sidenav.component';
import { LayoutModule } from '../shared/layout.module';

@NgModule({
  imports: [
    CommonModule,
    LayoutModule,
    OtherRoutingModule
  ],
  declarations: [
    SidenavComponent
  ],
})
export class OtherModule {
}
