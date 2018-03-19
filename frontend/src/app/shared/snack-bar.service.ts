import { Injectable } from '@angular/core';
import { MatSnackBar, MatSnackBarConfig } from '@angular/material';

@Injectable()
export class SnackBarService {

  constructor(private snackbar: MatSnackBar) {
  }

  open(message: string, properties?: MatSnackBarConfig): void {
    this.snackbar.open(message, 'ok', properties);
  }

  openError(message?: string, properties?: MatSnackBarConfig | null): void {
    if (!message) {
      message = 'Wystąpił problem, proszę spróbować ponownie';
    }
    if (!properties) {
      properties = {};
    }
    properties = this.addErrorCSS(properties);

    this.open(message, properties);
  }

  private addErrorCSS(properties: MatSnackBarConfig): MatSnackBarConfig {
    const errorCSS = 'snackbar-error';

    if (properties.panelClass) {
      if (typeof properties.panelClass === 'string') {
        properties.panelClass = [ errorCSS, properties.panelClass ];
      }
      if (properties.panelClass instanceof Array) {
        properties.panelClass.push(errorCSS);
      }
    } else {
      properties.panelClass = [ errorCSS ];
    }

    return properties;
  }
}
