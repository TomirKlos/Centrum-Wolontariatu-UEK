import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';

import { AppComponent } from './app.component';
import { AuthenticationService } from './shared/auth/authentication.service';
import { HttpClientModule } from '@angular/common/http';
import { AuthModule } from './auth/auth.module';
import { LayoutModule } from './shared/layout.module';
import { LoadingBarService } from './shared/loading-bar/loading-bar.service';
import { httpInterceptorProviders } from './shared/http-interceptors';
import { SnackBarService } from './shared/snack-bar.service';


@NgModule({
  declarations: [
    AppComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    LayoutModule,
    AuthModule
  ],
  providers: [
    AuthenticationService,
    LoadingBarService,
    SnackBarService,
    httpInterceptorProviders
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
