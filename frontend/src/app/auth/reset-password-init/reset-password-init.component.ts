import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { SnackBarService } from '../../shared/snack-bar.service';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { addValidator, matchValue } from '../../shared/validators';

@Component({
  selector: 'app-reset-password-init',
  templateUrl: './reset-password-init.component.html',
})
export class ResetPasswordInitComponent implements OnInit {
  resetPasswordInitForm: FormGroup;
  resetPasswordInitButtonDisabled = false;
  private emailValidators = [Validators.required, Validators.email];

  constructor(private fb: FormBuilder, private http: HttpClient, private sb: SnackBarService, private router: Router) {
  }

  ngOnInit() {
    this.resetPasswordInitForm = this.fb.group({
      email: ['', this.emailValidators]
    });
  }

  resetPasswordInit() {
    this.resetPasswordInitButtonDisabled = true;
    this.http.post(environment.apiEndpoint + 'reset-password/init', this.resetPasswordInitForm.value, { observe: 'response' })
      .subscribe(
        ok => {
          this.sb.open('Konto zostało utworzone. Proszę go aktywować linkiem wysłanym w emailu');
          this.router.navigateByUrl('/').then();
        },
        error => {
          this.dealWithErrorResponse(error);
          this.resetPasswordInitButtonDisabled = false;
        }
      );

  }

  private dealWithErrorResponse(err) {
    console.log(err);
    if (err && err.error && err.error.errorKey && err.error.errorKey === 'emailnotfound') {
      addValidator({
        oldValidators: this.emailValidators,
        newValidator: matchValue(this.resetPasswordInitForm.get('email').value),
        control: this.resetPasswordInitForm.get('email')
      });

    } else {
      this.sb.openError();
    }
  }
}
