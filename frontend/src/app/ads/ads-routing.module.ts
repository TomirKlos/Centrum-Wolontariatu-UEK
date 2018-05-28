import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';


import { AdminGuardService } from '../shared/auth/gurads/admin-guard.service';

import { AuthGuardService } from '../shared/auth/gurads/auth-guard.service';
import { AdsComponent } from './ads.component';
import { AddAdComponent } from './components/add-ad/add-ad.component';
import { AdminAdComponent } from './components/admin-ad/admin-ad.component';
import { MyAdssComponent } from './components/my-ads/my-ads.component';
import { ViewAdComponent } from './components/view-request/view-ad.component';
import { ViewAdResolverService } from './shared/ad-resolver.service';


const routes: Routes = [ {
  path: 'ads',
  component: AdsComponent,
  children: [
    { path: 'add', component: AddAdComponent, canActivate: [ AuthGuardService ] },
    { path: 'admin', component: AdminAdComponent, canActivate: [ AdminGuardService ] },
    { path: 'mine', component: MyAdssComponent, canActivate: [ AuthGuardService ] },
    { path: ':id', component: ViewAdComponent, resolve: { request: ViewAdResolverService }  },

  ]
} ];

@NgModule({
  imports: [ RouterModule.forChild(routes) ],
  exports: [ RouterModule ]
})
export class AdsRoutingModule {
}
