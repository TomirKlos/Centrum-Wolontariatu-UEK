import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material';

import {DialogData, VolunteerRequestVM} from '../../../shared/interfaces';


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
  viewApplyButton: boolean = false;


  constructor(
    public dialogRef: MatDialogRef<ViewRequestDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData) {
    this.request = data.volunteerRequest;
    this.viewApplyButton = data.showApplyButton;
  }

  replaceLineBreak(s: string) {
    return s && s.replace(/\n/g,' <br /> ');
  }

  showApplyForm() {
    this.viewApplyForm = !this.viewApplyForm;
  }
}
