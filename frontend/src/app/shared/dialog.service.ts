import {Component, Injectable} from '@angular/core';
import {MatDialog} from '@angular/material';

@Component({
})
@Injectable()
export class DialogService {
  constructor(public dialog: MatDialog) {}

  openDialog() {
    const dialogRef = this.dialog.open(DialogContentExampleDialog, {
      height: '350px'
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log(`Dialog result: ${result}`);
    });
  }
}
export class DialogContentExampleDialog {}