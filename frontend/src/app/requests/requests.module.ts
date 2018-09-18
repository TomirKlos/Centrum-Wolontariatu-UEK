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
import { ApplyRequestComponent } from './apply-request/apply-request.component';
import { ViewApplyRequestDialogComponent } from './components/my-requests/view-apply-request/view-apply-request-dialog.component';
import { MyRequestsService } from './components/my-requests/my-requests.service';
import { ApplyService } from './components/my-requests/view-apply-request/apply-request.service';
import {MatDatepickerModule, MatNativeDateModule, MatSelectModule} from '@angular/material';




@NgModule({
  imports: [
    LayoutModule,
    RequestsRoutingModule,
    MatSelectModule,
    MatDatepickerModule,
    MatNativeDateModule

  ],
  declarations: [
    RequestsComponent,
    AddRequestComponent,
    ViewRequestComponent,
    ViewRequestDialogComponent,
    AdminRequestsComponent,
    MyRequestsComponent,
    ApplyRequestComponent,
    ViewApplyRequestDialogComponent,

  ],
  providers: [
    RequestService,
    RequestDialogService,
    ViewRequestResolverService,
    MyRequestsService,
    ApplyService,
  ],
  entryComponents: [
    ViewRequestDialogComponent,
    ViewApplyRequestDialogComponent,
  ]
})
export class RequestsModule {
}
