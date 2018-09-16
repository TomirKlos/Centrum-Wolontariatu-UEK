import { NgModule } from '@angular/core';

import { LayoutModule } from '../shared/layout.module';

import { MatSelectModule } from '@angular/material';
import { AdsComponent } from './ads.component';
import { AdService } from './shared/ad.service';
import { AdsRoutingModule } from './ads-routing.module';
import { AddAdComponent } from './components/add-ad/add-ad.component';
import { AdminAdComponent } from './components/admin-ad/admin-ad.component';
import { MyAdssComponent } from './components/my-ads/my-ads.component';
import { MyAdsService } from './components/my-ads/my-ads.service';
import { AdDialogService } from './shared/ad-dialog.service';
import { ViewAdDialogComponent } from './components/view-request/view-ad-dialog.component';
import { ViewAdComponent } from './components/view-request/view-ad.component';
import { InviteAdComponent,  } from './components/invite-ad/invite-ad.component';
import { FormsModule } from '@angular/forms';
import { InviteChooseRequestDialogComponent } from './components/invite-ad/invite-chooseRequest-dialog.component';
import { ViewInvitationDialogComponent } from './components/view-volunteer-invitation/view-invitation-dialog.component';
import { InvitationService } from './components/view-volunteer-invitation/invitation.service';




@NgModule({
  imports: [
    AdsRoutingModule,
    LayoutModule,
    MatSelectModule,
    FormsModule

  ],
  declarations: [
    AddAdComponent,
    AdminAdComponent,
    MyAdssComponent,
    AdsComponent,
    ViewAdComponent,
    ViewAdDialogComponent,
    InviteAdComponent,
    InviteChooseRequestDialogComponent,
    ViewInvitationDialogComponent,
  ],
  providers: [
    AdService,
    MyAdsService,
    AdDialogService,
    InvitationService,


  ],
  entryComponents: [
    ViewAdDialogComponent,
    InviteChooseRequestDialogComponent,
    ViewInvitationDialogComponent



  ]
})
export class AdsModule {
}
