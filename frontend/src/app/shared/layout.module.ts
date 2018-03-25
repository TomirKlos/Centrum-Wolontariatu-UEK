import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MaterialModule } from './material.module';
import { ReactiveFormsModule } from '@angular/forms';
import { FlexLayoutModule } from '@angular/flex-layout';
import { HasAnyRoleDirective } from './auth/has-any-role.directive';

const myImports = [
  CommonModule,
  MaterialModule,
  ReactiveFormsModule,
  FlexLayoutModule,
];

const myDeclarations: any[] = [
  HasAnyRoleDirective
];

const myExports = myImports.concat(myDeclarations);

@NgModule({
  imports: myImports,
  exports: myExports,
  declarations: myDeclarations
})
export class LayoutModule {
}
