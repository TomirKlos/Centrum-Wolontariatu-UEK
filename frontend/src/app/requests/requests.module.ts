import { NgModule } from '@angular/core';

import { LayoutModule } from '../shared/layout.module';

import { RequestsRoutingModule } from './requests-routing.module';
import { RequestsComponent } from './requests.component';
import { AddRequestComponent } from './components/add-request/add-request.component';
import { ViewRequestComponent } from './components/view-request/view-request.component';
import { RequestService } from './shared/request.service';
import { ViewRequestResolverService } from './shared/request-resolver.service';
import { AdminRequestsComponent } from './components/admin-requests/admin-requests.component';
import { ViewRequestDialogComponent } from './components/view-request/view-request-dialog.component';
import { RequestDialogService } from './shared/request-dialog.service';
import { MyRequestsComponent } from './components/my-requests/my-requests.component';


@NgModule({
  imports: [
    LayoutModule,
    RequestsRoutingModule,
    
  ],
  declarations: [
    RequestsComponent,
    AddRequestComponent,
    ViewRequestComponent,
    ViewRequestDialogComponent,
    AdminRequestsComponent,
    MyRequestsComponent,
  ],
  providers: [
    RequestService,
    RequestDialogService,
    ViewRequestResolverService
  ],
  entryComponents: [
    ViewRequestDialogComponent
  ]
})
export class RequestsModule {
}
