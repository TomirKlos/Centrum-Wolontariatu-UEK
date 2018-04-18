import { CdkTableModule } from '@angular/cdk/table';
import { NgModule } from '@angular/core';
import {
  MatAutocompleteModule,
  MatButtonModule,
  MatCardModule,
  MatCheckboxModule,
  MatChipsModule,
  MatDialogModule,
  MatExpansionModule,
  MatFormFieldModule,
  MatIconModule,
  MatInputModule,
  MatListModule,
  MatMenuModule,
  MatPaginatorModule,
  MatProgressBarModule,
  MatSidenavModule,
  MatSnackBarModule,
  MatSortModule,
  MatTableModule,
  MatTabsModule,
  MatToolbarModule
} from '@angular/material';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

const modules = [
  BrowserAnimationsModule,
  MatToolbarModule,
  MatFormFieldModule,
  MatInputModule,
  MatButtonModule,
  MatProgressBarModule,
  MatCardModule,
  MatSnackBarModule,
  MatMenuModule,
  MatIconModule,
  MatTabsModule,
  MatSidenavModule,
  MatExpansionModule,
  MatTableModule,
  MatPaginatorModule,
  CdkTableModule,
  MatAutocompleteModule,
  MatDialogModule,
  MatCheckboxModule,
  MatSortModule,
  MatListModule,
  MatChipsModule
];

@NgModule({
  imports: modules,
  exports: modules
})
export class MaterialModule {
}
