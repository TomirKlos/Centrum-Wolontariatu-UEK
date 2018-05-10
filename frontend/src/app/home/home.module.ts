import { NgModule } from '@angular/core';
import { NguCarouselModule } from '@ngu/carousel';
import { MatAutocompleteModule } from '@angular/material';

@NgModule({
  imports: [
    NguCarouselModule,
    MatAutocompleteModule
  ],
  exports:[
    NguCarouselModule,
    MatAutocompleteModule
  ]
})
export class HomeModule {
}