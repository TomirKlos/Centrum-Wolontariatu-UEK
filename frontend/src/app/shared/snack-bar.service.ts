import { Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material';

@Injectable()
export class SnackBarService {

  constructor(private snackbar: MatSnackBar) {
  }

  open(message: string): void {
    this.snackbar.open(message, 'ok');
  }

  openError(message?: string): void {
    if (!message) {
      message = 'Wystąpił problem, proszę spróbować ponownie';
    }
    this.snackbar.open(message, 'ok', {
      panelClass: ['snackbar-error']
    });
  }
}
