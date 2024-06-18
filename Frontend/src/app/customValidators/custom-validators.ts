import {AbstractControl, ValidationErrors} from "@angular/forms";

export class CustomValidators {
  static mismaPassword(control: AbstractControl): ValidationErrors | null {
    const password2 = control.value
    const password = control.root.get('password')?.value
    if(password2 != password && password2.length > 0){
      return {mismaPassword: true}
    }else{
      return null
    }

  }
}
