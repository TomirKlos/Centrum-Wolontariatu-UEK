import { Component } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, ValidatorFn, Validators } from '@angular/forms';
import { RegisterService } from './register.service';
import { SnackBarService } from '../../shared/snack-bar.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss'],
  providers: [RegisterService]
})
export class RegisterComponent {
  registerForm: FormGroup;
  signupButtonDisabled = false;

  emailValidators = [
    Validators.required,
    Validators.email,
  ];

  constructor(private fb: FormBuilder,
              private registerService: RegisterService,
              private  snackbarService: SnackBarService,
              private router: Router) {
    this.createForm();
  }

  signup() {
    this.signupButtonDisabled = true;
    const credentials = {
      email: this.registerForm.get('email').value,
      password: this.registerForm.get('passwords').get('password').value
    };

    this.registerService.createAccount(credentials).subscribe(
      ok => {
        this.snackbarService.open('Rejestracja przebiegła pomyślnie, proszę aktywować konto linkiem wysłanym na wskazany adres email.');
        this.router.navigateByUrl('/').then();
      },
      e => {
        if (e && e.error && e.error.errorKey && e.error.errorKey === 'emailexists') {
          emailExistsError();
        } else {
          this.snackbarService.openError();
          console.log('coś wyjebało, nie wiem co :/');
          console.log(e);
        }
        this.signupButtonDisabled = false;
      }
    );

    function emailExistsError() {
      const newValidators = this.emailValidators;
      newValidators.push(emailExist(this.registerForm.get('email').value));
      this.registerForm.get('email').setValidators(newValidators);
      this.registerForm.get('email').updateValueAndValidity();
    }
  }

  createForm() {
    this.registerForm = this.fb.group({
      email: ['', this.emailValidators],
      passwords: this.fb.group({
        password: [
          '',
          [
            Validators.required,
            Validators.minLength(8),
          ]
        ],
        password2: [
          '',
          [
            Validators.required,
            isEqualeToOthersValidator
          ]
        ]
      })
    });
  }


}


function isEqualeToOthersValidator(control: AbstractControl): { [key: string]: any } {
  const error = {
    isEqual: true
  };

  if (!control || !control.parent) {
    return error;
  }

  const controls = Object.keys(control.parent.controls);
  let lastValue: string;
  for (const name of controls) {
    const value = control.parent.controls[name].value;

    if (lastValue && value !== lastValue) {
      return error;
    }
    lastValue = value;
  }

  return null;
}

export function emailExist(email: string): ValidatorFn {
  return (control: AbstractControl): { [key: string]: any } => {
    console.log('dupa', email, control.value);
    return (email === control.value) ? {'emailexists': {value: control.value}} : null;
  };
}
