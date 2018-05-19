import { Injectable } from '@angular/core';
import { MatDialog } from '@angular/material';

import { ViewRequestDialogComponent } from '../components/view-request/view-request-dialog.component';

@Injectable()
export class RequestDialogService {

  constructor(private _dialog: MatDialog) {
  }

  open(data: any) {
    return this._dialog.open(ViewRequestDialogComponent, {
      data: data,
      panelClass: 'app-dialog-container'
    });
  }
}
