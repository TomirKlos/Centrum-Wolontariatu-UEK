import { Component, Inject } from "@angular/core";
import { MAT_DIALOG_DATA, MatDialogRef } from "@angular/material";
import { VolunteerRequestVM } from "../../shared/interfaces";

@Component({
  selector: 'app-view-request-dialog',
  templateUrl: './view-request.component.html',
  styleUrls: [ './view-request.component.scss' ]
})
export class ViewRequestDialogComponent {
  request: VolunteerRequestVM;

  constructor(
    public dialogRef: MatDialogRef<ViewRequestDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any) {
    this.request = data;
  }

  onNoClick(): void {
    this.dialogRef.close();
  }

}
