import { NgModule } from '@angular/core';

import { RequestsRoutingModule } from './requests-routing.module';
import { RequestsComponent } from './requests.component';
import { AddRequestComponent } from './add-request/add-request.component';
import { LayoutModule } from "../shared/layout.module";
import { ViewRequestComponent } from './view-request/view-request.component';
import { RequestsService } from "./requests.service";
import { ViewRequestResolverService } from "./view-request/view-request-resolver.service";

@NgModule({
  imports: [
    LayoutModule,
    RequestsRoutingModule
  ],
  declarations: [
    RequestsComponent,
    AddRequestComponent,
    ViewRequestComponent,
  ],
  providers: [
    RequestsService,
    ViewRequestResolverService
  ]
})
export class RequestsModule {
}
