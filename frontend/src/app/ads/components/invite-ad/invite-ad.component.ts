import { Component, OnInit, Input, Inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

import { environment } from '../../../../environments/environment';
import { SnackBarService } from '../../../shared/snack-bar.service';
import { MatDialog } from '@angular/material';
import { InviteChooseRequestDialogComponent } from './invite-chooseRequest-dialog.component';



@Component({
  selector: 'app-invite-ad',
  templateUrl: './invite-ad.component.html',
})
export class InviteAdComponent implements OnInit {

  @Input() id: number;
  formGroup: FormGroup;
  formGroupVM: FormGroup;

  submitButtonDisabled = false;
  volunteerRequestId: number = 72;

  vrequestId: number;
  name: string;



  constructor(private _fb: FormBuilder, private _http: HttpClient, private _sb: SnackBarService, public dialog: MatDialog) {
  }

  ngOnInit() {
    this.formGroup = this._fb.group({
      description: [ '', [ Validators.required ] ],
      volunteerRequestId: [this.vrequestId],
      volunteerAdId: [this.id],
    });
  }

  apply() {
    this.formGroupVM = this._fb.group({
      description: [ this.formGroup.value.description, [ Validators.required ] ],
      volunteerRequestId: [this.vrequestId],
      volunteerAdId: [this.id],
    });

    this.submitButtonDisabled = true;
    console.log(this.id)
    this._http.post(environment.apiEndpoint + '/vrequest/invite', this.formGroupVM.value).subscribe(
      () => {
        this._sb.open('Aplikacja została wysłana');
      },
      () => this._sb.warning()
    );
  }

  openDialog(): void {
    let dialogRef = this.dialog.open(InviteChooseRequestDialogComponent, {
      width: '250px',
      data: { name: this.name, vrequestId: this.vrequestId }
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
      this.vrequestId = result[0];
      this.name = result[1];
    });
  }

}
