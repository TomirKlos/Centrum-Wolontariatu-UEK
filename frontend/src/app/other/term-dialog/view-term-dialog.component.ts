import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material';

@Component({
  selector: 'app-view-term-dialog',
  templateUrl: './view-term.component.html',
  styleUrls: [ './view-term.component.scss' ]
})
export class ViewTermDialogComponent {

  constructor(
    public dialogRef: MatDialogRef<ViewTermDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any) {
  }
}
