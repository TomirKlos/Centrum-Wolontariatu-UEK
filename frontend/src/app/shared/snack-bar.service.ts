import { Injectable } from '@angular/core';
import { MatSnackBar, MatSnackBarConfig } from '@angular/material';

@Injectable()
export class SnackBarService {
  private _defaultProperties: MatSnackBarConfig = {
    verticalPosition: 'top',
    horizontalPosition: 'end'
  };
  private _defaultWaringProperties: MatSnackBarConfig = {
    panelClass: ['snackbar-error', 'as']
  };
  private _defaultAction = 'ok';
  private _defaultWarnMessage = 'Wystąpił problem, proszę spróbować ponownie';


  constructor(private _snackbar: MatSnackBar) {
  }

  open(message: string, properties?: MatSnackBarConfig): void {
    if (window.innerWidth < 600) {
      if (!properties) {
        properties = {};
      }
      properties.verticalPosition = 'bottom';
    }

    this._snackbar.open(
      message,
      this._defaultAction,
      Object.assign({}, this._defaultProperties, properties)
    );
  }

  warning(message?: string, properties?: MatSnackBarConfig | null): void {
    if (!message) {
      message = this._defaultWarnMessage;
    }

    this.open(
      message,
      Object.assign({}, this._defaultWaringProperties, properties)
    );
  }

}
