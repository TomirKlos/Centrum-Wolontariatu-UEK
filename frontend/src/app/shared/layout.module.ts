import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MaterialModule } from './material.module';
import { ReactiveFormsModule } from '@angular/forms';
import { FlexLayoutModule } from '@angular/flex-layout';

const modules = [
  CommonModule,
  MaterialModule,
  ReactiveFormsModule,
  FlexLayoutModule
];


@NgModule({
  imports: modules,
  exports: modules,
  declarations: []
})
export class LayoutModule {
}
