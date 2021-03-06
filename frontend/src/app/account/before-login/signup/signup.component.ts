import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';

import { environment } from '../../../../environments/environment';
import { SnackBarService } from '../../../shared/snack-bar.service';
import { addValidator, matchValue } from '../../../shared/validators';
import {TermDialogService} from '../../../other/term-dialog/term-dialog.service';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
})
export class SignupComponent implements OnInit {
  formGroup: FormGroup;
  buttonDisabled = false;
  private _emailValidators = [ Validators.required, Validators.email, Validators.pattern(/.*(uek.krakow.pl)$/) ];
  private _userDataValidators = [ Validators.required ];
  private _termValidators = [ Validators.pattern('true') ];

  constructor(private _fb: FormBuilder, private _http: HttpClient, private _sb: SnackBarService, private _router: Router, private _termDialogService: TermDialogService) {
  }

  ngOnInit() {
    this.formGroup = this._fb.group({
      email: [ '', this._emailValidators ],
      firstName: ['', this._userDataValidators],
      lastName: ['', this._userDataValidators],
      term: [false, this._termValidators]
    });
  }

  signup() {
    this.buttonDisabled = true;
    const body = {
      email: this.formGroup.get('email').value,
      password: this.formGroup.get('passwords').get('password').value,
      firstName: this.formGroup.get('firstName').value,
      lastName: this.formGroup.get('lastName').value,
    };

    this._http.post(environment.apiEndpoint + '/register', body, { observe: 'response' })
      .subscribe(
        () => {
          this._sb.open('Konto zostało utworzone. Proszę go aktywować linkiem wysłanym w emailu', { duration: 0 });
          this._router.navigateByUrl('/').then();
        },
        error => {
          this._dealWithErrorResponse(error);
          this.buttonDisabled = false;
        }
      );
  }

  getPasswordsFormGroup(form: FormGroup) {
    this.formGroup.addControl('passwords', form);
  }

  private _dealWithErrorResponse(err) {
    if (err && err.error && err.error.errorKey && err.error.errorKey === 'emailexists') {
      addValidator({
        control: this.formGroup.get('email'),
        newValidator: matchValue(this.formGroup.get('email').value),
        oldValidators: this._emailValidators
      });
    } else {
      this._sb.warning();
    }
  }

  openTermDialog() {
    this._termDialogService.open();
  }
}
