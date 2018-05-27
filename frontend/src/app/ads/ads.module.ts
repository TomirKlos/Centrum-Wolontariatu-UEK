import { NgModule } from '@angular/core';

import { LayoutModule } from '../shared/layout.module';

import { MatSelectModule } from '@angular/material';
import { AdsComponent } from './ads.component';
import { AdService } from './shared/ad.service';
import { AdsRoutingModule } from './ads-routing.module';
import { AddAdComponent } from './components/add-ad/add-ad.component';
import { AdminAdComponent } from './components/admin-ad/admin-ad.component';




@NgModule({
  imports: [
    AdsRoutingModule,
    LayoutModule,
    MatSelectModule
    
  ],
  declarations: [
    AddAdComponent,
    AdminAdComponent
    

  ],
  providers: [
    AdService,
    

  ],
  entryComponents: [

  ]
})
export class AdsModule {
}
