import {AfterViewInit, Component, Inject, OnInit, ViewChild} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef, MatSort, MatPaginator, PageEvent, MatSortable} from '@angular/material';

import { responseVolunteerRequestVM } from '../../../../shared/interfaces';
import { ServerDataSource } from '../../../../shared/server-data-source';
import { ApplyService } from './apply-request.service';
import { SnackBarService } from '../../../../shared/snack-bar.service';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {merge} from 'rxjs/observable/merge';
import {tap} from 'rxjs/operators';

@Component({
  selector: 'app-apply-view-request-dialog',
  templateUrl: './view-apply-request.component.html',
})
export class ViewApplyRequestDialogComponent implements OnInit, AfterViewInit {
    application: number;
    showApply: boolean = false;
    showConfirmApply:boolean = false;
    applyToShow: responseVolunteerRequestVM;
    formGroup: FormGroup;

    //data source
    dataSource: ServerDataSource<responseVolunteerRequestVM>;
    columnsToDisplay = [ 'id', 'accepted', 'confirmed', 'description', 'showApply' ];

    totalElements: number;
    pageIndex = 0;
    pageSize = 5;

    pageEvent: PageEvent;

    @ViewChild(MatPaginator) paginator: MatPaginator;
    @ViewChild(MatSort) sort: MatSort;

  constructor(
    public dialogRef: MatDialogRef<ViewApplyRequestDialogComponent>,
    private _applyService: ApplyService,
    public _snackBar: SnackBarService,
    private _fb: FormBuilder,

    @Inject(MAT_DIALOG_DATA) public data: number) {
      console.log(data.valueOf)
    this.application = data;

    this.formGroup = this._fb.group({
      feedback: [ '', [ Validators.required ] ],
    });
  }
  ngOnInit() {
    this.sort.sort(<MatSortable>({id: 'id', start: 'desc'}));
    this.paginator.pageSize = this.pageSize;
    this.dataSource = new ServerDataSource<responseVolunteerRequestVM>(this._applyService, this.paginator, this.sort, "applications");
    this.dataSource.relativePathToServerResource = '';
    this.dataSource.loadApplicationPage(this.application);

    this.dataSource.connectToSourceElementsNumber().subscribe(d => {
      this.totalElements = d;
    });
  }

  ngAfterViewInit(){
    merge(this.sort.sortChange, this.paginator.page)
      .pipe(
        tap(() => this.dataSource.loadApplicationPage(this.application))
      )
      .subscribe( );
    this.dataSource.initAfterViewInit();
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
    this._applyService.confirm(id, this.formGroup.get('feedback').value).subscribe(() => {
      this.dataSource.loadApplicationPage(this.application),
      this.prepareConfirm(id);
      this.showApply = false;
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
