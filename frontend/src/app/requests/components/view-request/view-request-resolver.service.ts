import { Injectable } from '@angular/core';
import { Resolve, Router } from '@angular/router';
import { ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router/src/router_state';

import { Observable } from 'rxjs/Observable';
import { catchError } from 'rxjs/operators';

import { RequestsService } from '../../shared/requests.service';
import { VolunteerRequestVM } from '../../../shared/interfaces';


@Injectable()
export class ViewRequestResolverService implements Resolve<VolunteerRequestVM> {

  constructor(private _vRequestService: RequestsService, private _router: Router) {
  }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<VolunteerRequestVM> {
    const id = Number.parseInt(route.paramMap.get('id'));

    if (!id) {
      this._return404();
      return null;
    }

    return this._vRequestService.getOne(id).pipe(
      catchError(() => {
        this._return404();
        return Observable.of(null);
      })
    );
  }

  private _return404() {
    this._router.navigateByUrl('/404').then();
  }

}
