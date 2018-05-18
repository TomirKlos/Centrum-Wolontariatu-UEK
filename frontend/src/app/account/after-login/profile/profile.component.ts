import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../../environments/environment';
import { SnackBarService } from '../../../shared/snack-bar.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {
  formGroup: FormGroup;
  buttonDisabled = false;

  constructor(private fb: FormBuilder, private http: HttpClient, private sb: SnackBarService) {
  }

  ngOnInit() {
    this.formGroup = this.fb.group({});
  }

  submit() {
    const body = {
      password: this.formGroup.get('passwords').get('password').value,
    };

    this.http.post(environment.apiEndpoint + 'account/change-password', body, { observe: 'response' })
      .subscribe(
        ok => {
          this.sb.open('Hasło zostało zmienione');
          // todo add form reset
        },
        err => {
          this.sb.warning();
        }
      );
  }

  getPasswordsFormGroup(form: FormGroup) {
    this.formGroup.addControl('passwords', form);
  }
}
