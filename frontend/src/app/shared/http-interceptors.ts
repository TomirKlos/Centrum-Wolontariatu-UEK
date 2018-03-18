/* "Barrel" of Http Interceptors */
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { LoadingBarInterceptorService } from './loading-bar/loading-bar-interceptor.service';


/** Http interceptor providers in outside-in order */
export const httpInterceptorProviders = [
  {provide: HTTP_INTERCEPTORS, useClass: LoadingBarInterceptorService, multi: true},
];
