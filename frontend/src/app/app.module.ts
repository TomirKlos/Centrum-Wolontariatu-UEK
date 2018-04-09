import { HttpClientModule } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { JwtModule } from '@auth0/angular-jwt';

import { AccountModule } from './account/account.module';
import { AdminModule } from './admin/admin.module';
import { DialogComponent } from './admin/volunteer-request/dialog/dialog.component';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { PageNotFoundComponent } from './other/page-not-found/page-not-found.component';
import { AuthModule, jwtModuleOptions } from './shared/auth/auth.module';
import { DialogService } from './shared/dialog.service';
import { httpInterceptorProviders } from './shared/http-interceptors';
import { LayoutModule } from './shared/layout.module';
import { LoadingBarService } from './shared/loading-bar/loading-bar.service';
import { SnackBarService } from './shared/snack-bar.service';
import { OtherModule } from "./other/other.module";
import { RequestsModule } from "./requests/requests.module";
import { HomeModule } from "./home/home.module";

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
    DialogService,
    httpInterceptorProviders
  ],
  entryComponents: [
    DialogComponent
  ],
  bootstrap: [ AppComponent ]
})
export class AppModule {
}


