import { AbstractControl, ValidatorFn } from '@angular/forms';

export function matchOtherControls(): ValidatorFn {
  return (control: AbstractControl): { [key: string]: any } => {
    if (!control || !control.parent || !control.parent.controls) {
      return null;
    }

    let lastValue: string;
    for (const name of Object.keys(control.parent.controls)) {
      const value = control.parent.controls[name].value;
      if (lastValue !== undefined && value !== lastValue) {
        return {
          mathOther: true
        };
      }
      lastValue = value;
    }
    return null;
  };
}

export function matchValue(value: string | number): ValidatorFn {
  return (control: AbstractControl): { [key: string]: any } => {
    if (!control) {
      return null;
    }

    if (control.value === value) {
      return {
        matchValue: true
      };
    }
    return null;
  };
}

export function addValidator(o: AddValidatorProperty) {
  const tempValidators = o.oldValidators;
  tempValidators.push(o.newValidator);
  o.control.setValidators(tempValidators);
  o.control.updateValueAndValidity();
}

interface AddValidatorProperty {
  oldValidators: ValidatorFn[];
  newValidator: ValidatorFn;
  control: AbstractControl;
}

