import { HttpClientModule } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { JwtModule } from '@auth0/angular-jwt';

import { AccountModule } from './account/account.module';
import { AdminModule } from './admin/admin.module';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NavbarComponent } from './other/navbar/navbar.component';
import { PageNotFoundComponent } from './other/page-not-found/page-not-found.component';
import { AuthModule, jwtModuleOptions } from './shared/auth/auth.module';
import { httpInterceptorProviders } from './shared/http-interceptors';
import { LayoutModule } from './shared/layout.module';
import { LoadingBarService } from './shared/loading-bar/loading-bar.service';
import { SnackBarService } from './shared/snack-bar.service';
import { OtherModule } from './other/other.module';
import { RequestsModule } from './requests/requests.module';
import { HomeModule } from './home/home.module';
import { ListOfRequestModule } from './layouts/list-of-request/list-of-request.module';
import { NguCarouselModule } from '@ngu/carousel';
import {MatAutocompleteModule, MatSelectModule} from '@angular/material';
import { SearchService } from './shared/search-service.service';
import { AdsModule } from './ads/ads.module';
import { FooterComponent } from './other/footer/footer.component';
import { FlexLayoutModule } from '@angular/flex-layout';


@NgModule({
  declarations: [
    AppComponent,
    PageNotFoundComponent,
    NavbarComponent,
    FooterComponent,

  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    LayoutModule,
    AuthModule,
    JwtModule.forRoot(jwtModuleOptions),
    ListOfRequestModule,
    NguCarouselModule,
    MatAutocompleteModule,


    // module of app parts
    HomeModule,
    AccountModule,
    AdminModule,
    RequestsModule,
    AdsModule,

    OtherModule,
    AppRoutingModule,
    FlexLayoutModule,
    MatSelectModule
  ],
  providers: [
    LoadingBarService,
    SnackBarService,
    httpInterceptorProviders,
    SearchService,
  ],

  bootstrap: [AppComponent],
})
export class AppModule {
}


