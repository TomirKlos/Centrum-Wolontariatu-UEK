import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

import { GenericService } from '../../shared/generic.service';
import { Certificate } from '../../shared/interfaces';
import { SnackBarService } from '../../shared/snack-bar.service';


@Injectable()
export class CertificatesService extends GenericService<Certificate> {

  constructor(_http: HttpClient, _snackBar: SnackBarService) {
    super(_http, _snackBar);
    this._url = this._url + '/certificate';

  }

  public getCertificates() {
    return this._http.get(this._url);
  }

  public async downloadResource(): Promise<Blob> {
    const file =  await this._http.get<Blob>(
      this._url + '/uncertified/download',
      {responseType: 'blob' as 'json'}).toPromise();
    return file;
  }

  public async downloadSingleCsv(id: number): Promise<Blob> {
    const file =  await this._http.get<Blob>(
      this._url + '/uncertified/download/' + id,
      {responseType: 'blob' as 'json'}).toPromise();
    return file;
  }

}
