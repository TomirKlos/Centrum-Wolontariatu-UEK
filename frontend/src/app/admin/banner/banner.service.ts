import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

import { GenericService } from '../../shared/generic.service';
import {Banner, Category} from '../../shared/interfaces';
import { SnackBarService } from '../../shared/snack-bar.service';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';

@Injectable()
export class BannerService extends GenericService<Banner> {

  private _pageSubjectCategory = new BehaviorSubject<Banner>(null);

  constructor(_http: HttpClient, _snackBar: SnackBarService) {
    super(_http, _snackBar);
    this._url = this._url + '/carouselbanner';
  }

  getBanners() {
    return this._http.get(this._url + '/' );
  }

  createBanner(banner: Banner) {
    return this._http.post(this._url + '/', { banner });
  }

  makeBannerUp(id: number) {
    return this._http.post(this._url + '/up/' + id, { });
  }

  makeBannerDown(id: number) {
    return this._http.post(this._url + '/down/' + id, { });
  }
}
