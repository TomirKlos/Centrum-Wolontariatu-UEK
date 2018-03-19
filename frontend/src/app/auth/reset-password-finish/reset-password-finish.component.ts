import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { SnackBarService } from '../../shared/snack-bar.service';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { matchOtherControls } from '../../shared/validators';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-reset-password-finish',
  templateUrl: './reset-password-finish.component.html',
})
export class ResetPasswordFinishComponent implements OnInit {
  resetPasswordFinishForm: FormGroup;
  resetPasswordFinishButtonDisabled = false;
  private resetKey: string;

  constructor(private fb: FormBuilder,
              private http: HttpClient,
              private sb: SnackBarService,
              private router: Router,
              private route: ActivatedRoute) {
  }

  ngOnInit() {
    this.resetPasswordFinishForm = this.fb.group({
      password: ['', [Validators.required, Validators.minLength(8)]],
      password2: ['', [Validators.required, matchOtherControls]]
    });

    this.resetKey = this.route.snapshot.paramMap.get('reset-key');
  }

  resetPasswordFinishSubmit() {
    this.resetPasswordFinishButtonDisabled = true;
    const body = {
      key: this.resetKey,
      newPassword: this.resetPasswordFinishForm.get('password').value
    };

    console.log(body);
    this.http.post(environment.apiEndpoint + 'reset-password/finish', body, { observe: 'response' })
      .subscribe(
        ok => {
          this.sb.open('Hasło zostało zmienione', { duration: 3000 });
          this.router.navigateByUrl('/auth/login').then();
        },
        err => {
          console.log(err);
          this.resetPasswordFinishButtonDisabled = false;
          this.sb.openError();
          this.router.navigateByUrl('/').then();
        }
      );
  }
}
