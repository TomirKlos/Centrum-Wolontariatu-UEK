import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';

import { AppComponent } from './app.component';
import { AccountModule } from './account/account.module';
import { LayoutModule } from './shared/layout.module';
import { httpInterceptorProviders } from './shared/http-interceptors';
import { SnackBarService } from './shared/snack-bar.service';
import { HomeComponent } from './home/home.component';
import { PageNotFoundComponent } from './page-not-found/page-not-found.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { HttpClientModule } from '@angular/common/http';
import { LoadingBarService } from './shared/loading-bar/loading-bar.service';
import { AuthModule, jwtModuleOptions } from './shared/auth/auth.module';
import { JwtModule } from '@auth0/angular-jwt';
import { DumpComponent } from './dump/dump.component';
import { AdminModule } from './admin/admin.module';
import { SidenavComponent } from './layouts/sidenav/sidenav.component';


@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    PageNotFoundComponent,
    NavbarComponent,
    SidenavComponent,
    DumpComponent,
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    LayoutModule,
    AuthModule,
    JwtModule.forRoot(jwtModuleOptions),

    AppRoutingModule,

    AccountModule,
    AdminModule
  ],
  providers: [
    LoadingBarService,
    SnackBarService,
    httpInterceptorProviders
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}


