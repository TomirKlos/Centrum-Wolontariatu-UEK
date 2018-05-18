import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { SnackBarService } from '../../../shared/snack-bar.service';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../../environments/environment';

@Component({
  selector: 'app-reset-password-finish',
  templateUrl: './reset-password-finish.component.html',
})
export class ResetPasswordFinishComponent implements OnInit {
  formGroup: FormGroup;
  buttonDisabled = false;
  private resetKey: string;

  constructor(private fb: FormBuilder,
              private http: HttpClient,
              private sb: SnackBarService,
              private router: Router,
              private route: ActivatedRoute) {
  }

  ngOnInit() {
    this.resetKey = this.route.snapshot.paramMap.get('reset-key');

    this.formGroup = this.fb.group({});
  }

  getPasswordsFormGroup(form: FormGroup) {
    this.formGroup.addControl('passwords', form);
  }

  submit() {
    this.buttonDisabled = true;
    const body = {
      key: this.resetKey,
      newPassword: this.formGroup.get('passwords').get('password').value
    };

    this.http.post(environment.apiEndpoint + 'reset-password/finish', body, { observe: 'response' })
      .subscribe(
        ok => {
          this.sb.open('Hasło zostało zmienione', { duration: 3000 });
          this.router.navigateByUrl('/login').then();
        },
        err => {
          this.buttonDisabled = false;
          this.sb.warning();
          this.router.navigateByUrl('/').then();
        }
      );
  }


}
