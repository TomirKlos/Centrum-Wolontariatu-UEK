import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';


import { AdminGuardService } from '../shared/auth/gurads/admin-guard.service';

import { AuthGuardService } from '../shared/auth/gurads/auth-guard.service';
import { AdsComponent } from './ads.component';
import { AddAdComponent } from './components/add-ad/add-ad.component';


const routes: Routes = [ {
  path: 'ads',
  component: AdsComponent,
  children: [
    { path: 'add', component: AddAdComponent },

  ]
} ];

@NgModule({
  imports: [ RouterModule.forChild(routes) ],
  exports: [ RouterModule ]
})
export class AdsRoutingModule {
}
