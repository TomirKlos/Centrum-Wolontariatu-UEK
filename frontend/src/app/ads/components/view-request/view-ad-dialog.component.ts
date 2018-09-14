import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material';

import { VolunteerAdVM } from '../../../shared/interfaces';

@Component({
  selector: 'app-view-ad-dialog',
  templateUrl: './view-ad.component.html',
  styleUrls: [ './view-ad.component.scss' ]
})
export class ViewAdDialogComponent {
  ad: VolunteerAdVM;
  pathToStaticContent = "http://localhost:8080/static/";
  staticNotFoundImage = "http://localhost:8080/static/brak-obrazka.jpg"
  viewApplyForm: boolean = false;


  constructor(
    public dialogRef: MatDialogRef<ViewAdDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any) {
    this.ad = data;
  }

  replaceLineBreak(s:string) {
    return s && s.replace(/\n/g,' <br /> ');
  }

  showApplyForm(){
    this.viewApplyForm = !this.viewApplyForm;
  }

}
