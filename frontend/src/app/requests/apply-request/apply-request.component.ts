import { Component, OnInit, Input } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

import { environment } from '../../../environments/environment';
import { SnackBarService } from '../../shared/snack-bar.service';

import { ImageUploadModule } from "angular2-image-upload";
import { VolunteerRequestVM } from '../../shared/interfaces';


@Component({
  selector: 'app-apply-request',
  templateUrl: './apply-request.component.html',
})
export class ApplyRequestComponent implements OnInit {

  @Input() id: number;
  formGroup: FormGroup;

  submitButtonDisabled = false;

  constructor(private _fb: FormBuilder, private _http: HttpClient, private _sb: SnackBarService) {
  }

  ngOnInit() {
    this.formGroup = this._fb.group({
      description: [ '', [ Validators.required ] ],
      volunteerRequestId: [this.id],
    });
  }

  apply() {
    this.submitButtonDisabled = true;
    console.log(this.id)
    this._http.post(environment.apiEndpoint + '/responseVr/', this.formGroup.value).subscribe(
      () => {
        this._sb.open('Aplikacja została wysłana');
      },
      () => this._sb.warning()
    );
  }

}
