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
import { MatAutocompleteModule } from '@angular/material';
import { SearchService } from './shared/search-service.service';
import { RequestDialogService } from './requests/shared/request-dialog.service';
import { ViewRequestResolverService } from './requests/shared/request-resolver.service';
import { ViewRequestDialogComponent } from './requests/components/view-request/view-request-dialog.component';

@NgModule({
  declarations: [
    AppComponent,
    PageNotFoundComponent,
    NavbarComponent,
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

    OtherModule,
    AppRoutingModule,
  ],
  providers: [
    LoadingBarService,
    SnackBarService,
    httpInterceptorProviders,
    SearchService,
  ],

  bootstrap: [ AppComponent ]
})
export class AppModule {
}


