import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MaterialModule } from './material.module';
import { ReactiveFormsModule } from '@angular/forms';
import { FlexLayoutModule } from '@angular/flex-layout';
import { HasAnyRoleDirective } from './auth/has-any-role.directive';
import { MatAutocompleteModule } from '@angular/material';
import { NguCarouselModule } from '@ngu/carousel';

const myImports = [
  CommonModule,
  MaterialModule,
  ReactiveFormsModule,
  FlexLayoutModule,
  MatAutocompleteModule,
  NguCarouselModule,
];

const myDeclarations: any[] = [
  HasAnyRoleDirective,
];

const myExports = myImports.concat(myDeclarations);

@NgModule({
  imports: myImports,
  exports: myExports,
  declarations: myDeclarations
})
export class LayoutModule {
}
