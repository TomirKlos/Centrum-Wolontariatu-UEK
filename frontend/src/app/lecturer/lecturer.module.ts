import { NgModule } from '@angular/core';

import { LecturerRoutingModule } from './lecturer-routing.module';
import { LecturerComponent } from './lecturer.component';
import { AddRequestComponent } from './add-request/add-request.component';
import { LayoutModule } from "../shared/layout.module";

@NgModule({
  imports: [
    LayoutModule,
    LecturerRoutingModule
  ],
  declarations: [
    LecturerComponent,
    AddRequestComponent
  ]
})
export class LecturerModule {
}
