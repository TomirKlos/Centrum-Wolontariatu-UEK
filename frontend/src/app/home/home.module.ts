import { NgModule } from '@angular/core';

import { HomeRoutingModule } from './home-routing.module';
import { HomeComponent } from "./home.component";
import { ListOfRequestComponent } from "../layouts/list-of-request/list-of-request.component";
import { LayoutModule } from "../shared/layout.module";

@NgModule({
  imports: [
    LayoutModule,
    HomeRoutingModule
  ],
  declarations: [
    HomeComponent,
    ListOfRequestComponent
  ]
})
export class HomeModule {
}
