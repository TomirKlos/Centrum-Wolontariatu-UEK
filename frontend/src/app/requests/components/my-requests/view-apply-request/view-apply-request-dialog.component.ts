import { Component, Inject, ViewChild } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef, MatSort, MatPaginator } from '@angular/material';

import { VolunteerRequestVM, responseVolunteerRequestVM } from '../../../../shared/interfaces';
import { ServerDataSource } from '../../../../shared/server-data-source';
import { ApplyService } from './apply-request.service';
import { SnackBarService } from '../../../../shared/snack-bar.service';

@Component({
  selector: 'app-apply-view-request-dialog',
  templateUrl: './view-apply-request.component.html',
})
export class ViewApplyRequestDialogComponent {
    application: number;
    showApply: boolean = false;
    showConfirmApply:boolean = false;
    applyToShow: responseVolunteerRequestVM;

    //data source
    dataSource: ServerDataSource<responseVolunteerRequestVM>;
    columnsToDisplay = [ 'id', 'accepted', 'confirmed', 'description', 'showApply' ];

  constructor(
    public dialogRef: MatDialogRef<ViewApplyRequestDialogComponent>,
    private _applyService: ApplyService,
    public _snackBar: SnackBarService,

    @Inject(MAT_DIALOG_DATA) public data: number) {
      console.log(data.valueOf)
    this.application = data;

    this.dataSource = new ServerDataSource<responseVolunteerRequestVM>(this._applyService, null, new MatSort, "applications");
    this.dataSource.relativePathToServerResource = '';
    this.dataSource.loadApplicationPage(this.application);
    console.log(this.dataSource)
  }

  replaceLineBreak(s:string) {
    return s && s.replace(/\n/g,' <br /> ');
  }

  showApplication(response: responseVolunteerRequestVM){
    this.showApply = true;
    this.applyToShow = response;
  }

  changeAccepted(id: number) {
    this._applyService.accept(id).subscribe(() =>{
      this.dataSource.loadApplicationPage(this.application),
      this.showApply=false;
      this._snackBar.open('Zgłoszenie zostało zaakceptowane');

    });
  }

  disableAccept(id: number) {
    this._applyService.disableAccept(id).subscribe(() => {
      this.dataSource.loadApplicationPage(this.application),
      this.showApply=false;
      this._snackBar.open('Akceptacja zgłoszenia została cofnięta');
    });
  }

  confirm(id: number) {
    this._applyService.confirm(id).subscribe(() => {
      this.dataSource.loadApplicationPage(this.application),
      this.prepareConfirm(id);
      this.showApply=false;
      this._snackBar.open('Wolontariat został potwierdzony');
    });
  }

  closeApplication(id: number){
    this.showApply = false;
  }

  prepareConfirm(id: number){
    this.showConfirmApply = !this.showConfirmApply;
  }

}
