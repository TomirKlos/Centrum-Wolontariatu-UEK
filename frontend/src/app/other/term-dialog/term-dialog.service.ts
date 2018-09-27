import { Injectable } from '@angular/core';
import { MatDialog } from '@angular/material';
import {ViewTermDialogComponent} from './view-term-dialog.component';


@Injectable()
export class TermDialogService {

  constructor(private _dialog: MatDialog) {
  }

  open() {
    return this._dialog.open(ViewTermDialogComponent, {
      panelClass: 'app-dialog-container'
    });
  }
}
