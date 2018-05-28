import { Injectable } from '@angular/core';
import { MatDialog } from '@angular/material';
import { ViewAdDialogComponent } from '../components/view-request/view-ad-dialog.component';

@Injectable()
export class AdDialogService {

  constructor(private _dialog: MatDialog) {
  }

  open(data: any) {
    return this._dialog.open(ViewAdDialogComponent, {
      data: data,
      panelClass: 'app-dialog-container'
    });
  }

 /* openApplicationsPanel(id: number) {
    return this._dialog.open(ViewApplyRequestDialogComponent, {
      data: id,
      panelClass: 'app-dialog-container'
    });
  }*/
}
