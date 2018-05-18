import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../../shared/auth/auth.service';
import { Router } from '@angular/router';
import { SnackBarService } from '../../../shared/snack-bar.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  loginForm: FormGroup;
  loginButtonDisabled = false;

  constructor(private authService: AuthService,
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

    this.authService.login(this.loginForm.value).subscribe(
      loggedIn => {
        if (loggedIn) {
          this.sb.open('Pomyślnie zalogowano :d', { duration: 3000 });
          this.router.navigateByUrl(this.authService.redirectUrl).then();
        } else {
          this.loginButtonDisabled = false;
          this.sb.warning('Nieprawidłowy email lub hasło', { duration: 5000 });
        }
      },
      () => this.sb.warning()
    );
    this.loginButtonDisabled = false;
  }
}
