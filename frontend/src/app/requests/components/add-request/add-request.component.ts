import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

import { environment } from '../../../../environments/environment';
import { SnackBarService } from '../../../shared/snack-bar.service';

@Component({
  selector: 'app-add-request',
  templateUrl: './add-request.component.html',
})
export class AddRequestComponent implements OnInit {
  formGroup: FormGroup;
  submitButtonDisabled = false;

  constructor(private _fb: FormBuilder, private _http: HttpClient, private _sb: SnackBarService) {
  }

  ngOnInit() {
    this.formGroup = this._fb.group({
      title: [ '', [ Validators.required ] ],
      description: [ '', [ Validators.required ] ],
      forStudents: [ false ],
      forTutors: [ false ],
    });
  }

  submit() {
    this.submitButtonDisabled = true;
    this._http.post(environment.apiEndpoint + '/vrequest', this.formGroup.value).subscribe(
      () => {
        this._sb.open('Oferta została dodana');
      },
      () => this._sb.warning()
    );
  }

}
