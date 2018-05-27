import { NgModule } from '@angular/core';
import {CommonModule} from "@angular/common";
import { RelativeDatePipe } from './relativeDate.pipe';


@NgModule({
  declarations:[RelativeDatePipe],
  imports:[CommonModule],
  exports:[RelativeDatePipe]
})

export class MainPipe{}