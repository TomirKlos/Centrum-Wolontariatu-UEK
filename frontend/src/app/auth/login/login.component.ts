import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthenticationService } from '../../shared/auth/authentication.service';
import { Router } from '@angular/router';
import { SnackBarService } from '../../shared/snack-bar.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  loginForm: FormGroup;
  loginButtonDisabled = false;

  constructor(private authenticationService: AuthenticationService,
              private router: Router,
              private sb: SnackBarService,
              private fb: FormBuilder) {
  }

  ngOnInit() {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required]
    });
  }

  login() {
    this.loginButtonDisabled = true;

    this.authenticationService.login(this.loginForm.value).subscribe(d => {
        switch (d) {
          case 200: {
            this.sb.open('Pomyślnie zalogowano :d');
            this.router.navigateByUrl('/').then();
            break;
          }
          case 401: {
            this.loginButtonDisabled = false;
            this.sb.openError('Nieprawidłowy email lub hasło', { duration: 5000 });
            break;
          }
          default: {
            this.sb.openError(null, { duration: 5000 });
          }
        }
      }
    );

  }

}
