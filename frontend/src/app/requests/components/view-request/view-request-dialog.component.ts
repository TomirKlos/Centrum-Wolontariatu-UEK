import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material';

import { VolunteerRequestVM } from '../../../shared/interfaces';

@Component({
  selector: 'app-view-request-dialog',
  templateUrl: './view-request.component.html',
  styleUrls: [ './view-request.component.scss' ]
})
export class ViewRequestDialogComponent {
  request: VolunteerRequestVM;
  pathToStaticContent = "http://localhost:8080/static/";
  staticNotFoundImage = "http://localhost:8080/static/brak-obrazka.jpg"
  viewApplyForm: boolean = false;


  constructor(
    public dialogRef: MatDialogRef<ViewRequestDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any) {
    this.request = data;
  }

  replaceLineBreak(s:string) {
    return s && s.replace(/\n/g,' <br /> ');
  }

  showApplyForm(){
    this.viewApplyForm = !this.viewApplyForm;
  }
}
