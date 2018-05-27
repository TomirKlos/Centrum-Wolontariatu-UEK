import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { RequestsComponent } from './requests.component';
import { AddRequestComponent } from './components/add-request/add-request.component';
import { ViewRequestComponent } from './components/view-request/view-request.component';
import { ViewRequestResolverService } from './shared/request-resolver.service';
import { AdminRequestsComponent } from './components/admin-requests/admin-requests.component';
import { AdminGuardService } from '../shared/auth/gurads/admin-guard.service';
import { MyRequestsComponent } from './components/my-requests/my-requests.component';
import { AuthGuardService } from '../shared/auth/gurads/auth-guard.service';
import { ApplyRequestComponent } from './apply-request/apply-request.component';
import { ViewApplyRequestComponent } from './components/my-requests/view-apply-request/view-apply-request.component';

const routes: Routes = [ {
  path: 'requests',
  component: RequestsComponent,
  children: [
    { path: 'add', component: AddRequestComponent },
    { path: 'admin', component: AdminRequestsComponent, canActivate: [ AdminGuardService ] },
    { path: 'mine', component: MyRequestsComponent, canActivate: [ AuthGuardService ] },
    { path: ':id', component: ViewRequestComponent, resolve: { request: ViewRequestResolverService } },
    { path: 'apply', component: ApplyRequestComponent },
    { path: 'applications', component: ViewApplyRequestComponent },
    
    //todo add authGuardService to 'applications' path

  ]
} ];

@NgModule({
  imports: [ RouterModule.forChild(routes) ],
  exports: [ RouterModule ]
})
export class RequestsRoutingModule {
}
