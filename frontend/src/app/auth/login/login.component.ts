import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { AuthenticationService } from '../../shared/auth/authentication.service';
import { Router } from '@angular/router';


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
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
  errorMessage = {
    display: false,
    message: ''
  };

  constructor(private authenticationService: AuthenticationService, private router: Router) {
  }

  ngOnInit() {
  }

  login() {
    this.loginButtonDisabled = true;

    this.authenticationService.login(this.loginForm.value).subscribe(d => {
        switch (d) {
          case 200: {
            console.log('ok');
            this.router.navigateByUrl('/').then();
            break;
          }
          case 401: {
            this.loginButtonDisabled = false;
            this.errorMessage.message = 'Nieprawidłowy email lub hasło';
            this.errorMessage.display = true;
            break;
          }
          default: {
            this.errorMessage.message = 'Wystąpił błąd, proszę spróbować ponownie';
            this.errorMessage.display = true;
          }
        }
      }
    );

  }

}
