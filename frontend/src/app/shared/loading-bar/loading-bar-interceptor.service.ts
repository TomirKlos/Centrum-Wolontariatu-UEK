import { Injectable } from '@angular/core';
import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { LoadingBarService } from './loading-bar.service';
import { finalize } from 'rxjs/operators';

@Injectable()
export class LoadingBarInterceptorService implements HttpInterceptor {

  constructor(private loadingBarService: LoadingBarService) {
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    this.loadingBarService.turnOn();
    return next.handle(req)
      .pipe(
        // Log when response observable either completes or errors
        finalize(() => {
          this.loadingBarService.turnOff();
        })
      );
  }
}
