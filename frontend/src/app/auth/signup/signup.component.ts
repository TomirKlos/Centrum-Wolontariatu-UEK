import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { addValidator, matchOtherControls, matchValue } from '../../shared/validators';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { SnackBarService } from '../../shared/snack-bar.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
})
export class SignupComponent implements OnInit {
  signupForm: FormGroup;
  signupButtonDisabled = false;
  private emailValidators = [Validators.required, Validators.email, Validators.pattern(/.*(uek.krakow.pl)$/)];

  constructor(private fb: FormBuilder, private http: HttpClient, private sb: SnackBarService, private router: Router) {
  }

  ngOnInit() {
    this.signupForm = this.fb.group({
      email: ['', this.emailValidators],
      passwords: this.fb.group({
        password: ['', [Validators.required, Validators.minLength(8)]],
        password2: ['', [Validators.required, matchOtherControls()]]
      })
    });
  }

  signup() {
    this.signupButtonDisabled = true;
    const body = {
      email: this.signupForm.get('email').value,
      password: this.signupForm.get('passwords').get('password').value
    };

    this.http.post(environment.apiEndpoint + 'register', body, { observe: 'response' })
      .subscribe(
        ok => {
          this.sb.open('Konto zostało utworzone. Proszę go aktywować linkiem wysłanym w emailu');
          this.router.navigateByUrl('/').then();
        },
        error => {
          this.dealWithErrorResponse(error);
          this.signupButtonDisabled = false;
        }
      );
  }

  private dealWithErrorResponse(err) {
    if (err && err.error && err.error.errorKey && err.error.errorKey === 'emailexists') {
      addValidator({
        control: this.signupForm.get('email'),
        newValidator: matchValue(this.signupForm.get('email').value),
        oldValidators: this.emailValidators
      });
    } else {
      this.sb.openError();
    }
  }

}
