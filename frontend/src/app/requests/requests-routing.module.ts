import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { RequestsComponent } from "./requests.component";
import { AddRequestComponent } from "./add-request/add-request.component";
import { ViewRequestComponent } from "./view-request/view-request.component";
import { ViewRequestResolverService } from "./view-request/view-request-resolver.service";

const routes: Routes = [
  {
    path: 'requests',
    component: RequestsComponent,
    children: [
      { path: 'add', component: AddRequestComponent },
      { path: ':id', component: ViewRequestComponent, resolve: { request: ViewRequestResolverService } }
    ]
  }
];

@NgModule({
  imports: [ RouterModule.forChild(routes) ],
  exports: [ RouterModule ]
})
export class RequestsRoutingModule {
}
