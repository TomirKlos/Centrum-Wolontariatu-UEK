import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { LecturerComponent } from "./lecturer.component";
import { AddRequestComponent } from "./add-request/add-request.component";

const routes: Routes = [
  {
    path: 'requests',
    component: LecturerComponent,
    children: [
      { path: 'add', component: AddRequestComponent }
    ]
  }
];

@NgModule({
  imports: [ RouterModule.forChild(routes) ],
  exports: [ RouterModule ]
})
export class LecturerRoutingModule {
}
