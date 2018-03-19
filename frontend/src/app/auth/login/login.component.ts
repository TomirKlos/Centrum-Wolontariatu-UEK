import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { AuthenticationService } from '../../shared/auth/authentication.service';
import { Router } from '@angular/router';
import { SnackBarService } from '../../shared/snack-bar.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
})
export class LoginComponent implements OnInit {
  loginForm = new FormGroup({
    email: new FormControl('', [
      Validators.required,
      Validators.email,
    ]),
    password: new FormControl('', [
      Validators.required,
    ])
  });

  loginButtonDisabled = false;

  constructor(private authenticationService: AuthenticationService, private router: Router, private snackBarService: SnackBarService) {
  }

  ngOnInit() {
  }

  login() {
    this.loginButtonDisabled = true;

    this.authenticationService.login(this.loginForm.value).subscribe(d => {
        switch (d) {
          case 200: {
            this.snackBarService.open('Pomyślnie zalogowano :d');
            this.router.navigateByUrl('/').then();
            break;
          }
          case 401: {
            this.loginButtonDisabled = false;
            this.snackBarService.openError('Nieprawidłowy email lub hasło', { duration: 5000 });
            break;
          }
          default: {
            this.snackBarService.openError(null, { duration: 5000 });
          }
        }
      }
    );

  }

}
