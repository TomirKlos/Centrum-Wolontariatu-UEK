import { NgModule} from '@angular/core';
import { CommonModule } from '@angular/common';

import { OtherRoutingModule } from './other-routing.module';
import { SidenavComponent } from './sidenav/sidenav.component';
import { LayoutModule } from '../shared/layout.module';
import { ViewTermDialogComponent } from './term-dialog/view-term-dialog.component';
import { TermDialogService } from './term-dialog/term-dialog.service';

@NgModule({
  imports: [
    CommonModule,
    LayoutModule,
    OtherRoutingModule
  ],
  declarations: [
    SidenavComponent,
    ViewTermDialogComponent
  ],
  providers: [
    TermDialogService,
  ],
  entryComponents: [
    ViewTermDialogComponent,
  ],
})
export class OtherModule {
}
