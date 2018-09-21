import { Component, OnInit, Input } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

import { environment } from '../../../environments/environment';
import { SnackBarService } from '../../shared/snack-bar.service';



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
      err => {
        console.log(err.error.errorKey);
        if ( err.error.errorKey === 'cannotaplyforownvolunteerrequest') {
          this._sb.warning('Nie możesz dolaczyc do wlasnego ogloszenia!', { duration: 5000 });
        } else if ( err.error.errorKey === 'cannotaplyforvolunteerrequestbecauseofstatus'){
          this._sb.warning('Nie posiadasz wymaganego w ogloszeniu statusu pracownika/studenta!', { duration: 5000 });
        } else this._sb.warning();

      }
    );
  }

}
