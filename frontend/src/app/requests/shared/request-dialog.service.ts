import { Injectable } from '@angular/core';
import { ViewRequestDialogComponent } from '../components/view-request/view-request-dialog.component';
import { MatDialog } from '@angular/material';

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
