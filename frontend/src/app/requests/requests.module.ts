import { NgModule } from '@angular/core';

import { LayoutModule } from '../shared/layout.module';

import { RequestsRoutingModule } from './requests-routing.module';
import { RequestsComponent } from './requests.component';
import { AddRequestComponent } from './components/add-request/add-request.component';
import { ViewRequestComponent } from './components/view-request/view-request.component';
import { RequestsService } from './shared/requests.service';
import { ViewRequestResolverService } from './components/view-request/view-request-resolver.service';
import { AdminRequestsComponent } from './components/admin-requests/admin-requests.component';
import { ViewRequestDialogComponent } from './components/view-request/view-request-dialog.component';
import { RequestDialogService } from './shared/request-dialog.service';

@NgModule({
  imports: [
    LayoutModule,
    RequestsRoutingModule
  ],
  declarations: [
    RequestsComponent,
    AddRequestComponent,
    ViewRequestComponent,
    ViewRequestDialogComponent,
    AdminRequestsComponent,
  ],
  providers: [
    RequestsService,
    RequestDialogService,
    ViewRequestResolverService
  ],
  entryComponents: [
    ViewRequestDialogComponent
  ]
})
export class RequestsModule {
}
