import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { matchOtherControls } from '../../../shared/validators';

@Component({
  selector: 'app-passwords-form-group',
  templateUrl: './passwords-form-group.component.html',
})
export class PasswordsFormGroupComponent implements OnInit {
  @Output() passwordsFormEmitter = new EventEmitter<FormGroup>();
  passwordsFormGroup: FormGroup;

  constructor(private fb: FormBuilder) {
  }

  ngOnInit() {
    this.passwordsFormGroup = this.fb.group({
      password: ['', [Validators.required, Validators.minLength(8)]],
      password2: ['', [Validators.required, matchOtherControls()]]
    });

    this.passwordsFormGroup.get('password').valueChanges.subscribe(() => this.passwordsFormGroup.get('password2').updateValueAndValidity());

    this.passwordsFormEmitter.emit(this.passwordsFormGroup);
  }

}
