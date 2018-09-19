import { NgModule } from '@angular/core';

import { HomeRoutingModule } from './home-routing.module';
import { HomeComponent } from './home.component';
import { ListOfRequestComponent } from '../layouts/list-of-request/list-of-request.component';
import { LayoutModule } from '../shared/layout.module';
import {RelativeDatePipe, RelativeDatePipeExpiration} from '../shared/relativeDate.pipe';
import {
  MatSelectModule, MatSlideToggleModule,
  MatTooltipModule
} from '@angular/material';
import {FormsModule} from '@angular/forms';

@NgModule({
  imports: [
    LayoutModule,
    HomeRoutingModule,
    MatTooltipModule,
    MatSelectModule,
    MatSlideToggleModule,
    FormsModule

  ],
  declarations: [
    HomeComponent,
    ListOfRequestComponent,
    RelativeDatePipe,
    RelativeDatePipeExpiration
  ]
})
export class HomeModule {
}
