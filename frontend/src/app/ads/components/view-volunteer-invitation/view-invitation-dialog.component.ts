import {AfterViewInit, Component, Inject, OnInit, ViewChild} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef, MatSort, MatPaginator, MatSortable} from '@angular/material';
import {InvitationVolunteerRequestVM, responseVolunteerRequestVM, VolunteerRequestVM} from '../../../shared/interfaces';
import { ServerDataSource } from '../../../shared/server-data-source';
import { InvitationService } from './invitation.service';
import { SnackBarService } from '../../../shared/snack-bar.service';
import {tap} from 'rxjs/operators';
import {merge} from 'rxjs/observable/merge';


@Component({
  selector: 'app-view-invitation-dialog',
  templateUrl: './view-invitation.component.html',
})
export class ViewInvitationDialogComponent implements OnInit, AfterViewInit{
    application: number;
    showApply: boolean = false;
    showConfirmApply:boolean = false;
    invitationToShow: InvitationVolunteerRequestVM;

    //paginator
    totalElements: number;
    pageIndex = 0;
    pageSize = 5;

    //data source
    dataSource: ServerDataSource<InvitationVolunteerRequestVM>;
    columnsToDisplay = [ 'id', 'accepted', 'description', 'showApply' ];

    @ViewChild(MatPaginator) paginator: MatPaginator;
    @ViewChild(MatSort) sort: MatSort;


  constructor(
    public dialogRef: MatDialogRef<ViewInvitationDialogComponent>,
    private _invitationService: InvitationService,
    public _snackBar: SnackBarService,

    @Inject(MAT_DIALOG_DATA) public data: number) {
      console.log(data.valueOf)
    this.application = data;

  /*  this.dataSource = new ServerDataSource<InvitationVolunteerRequestVM>(this._invitationService, null, new MatSort, "invitations");
    this.dataSource.relativePathToServerResource = '';
    this.dataSource.loadInvitationPage(this.application);
    console.log(this.dataSource) */
  }

  ngOnInit() {
    this.sort.sort(<MatSortable>({id: 'id', start: 'desc'}));
    this.paginator.pageSize = this.pageSize;
    this.dataSource = new ServerDataSource<InvitationVolunteerRequestVM>(this._invitationService, this.paginator, this.sort, "invitations");
    this.dataSource.relativePathToServerResource = '';
    this.dataSource.loadInvitationPage(this.application);
    console.log(this.dataSource);

    this.dataSource.connectToSourceElementsNumber().subscribe(d => {
      this.totalElements = d;
    });
  }

  ngAfterViewInit() {
    merge(this.sort.sortChange, this.paginator.page)
      .pipe(
        tap(() => this.dataSource.loadInvitationPage(this.application))
      )
      .subscribe( );
    this.dataSource.initAfterViewInit();
  }

  replaceLineBreak(s: string) {
    return s && s.replace(/\n/g,' <br /> ');
  }

  showApplication(invitation: InvitationVolunteerRequestVM){
    this.showApply = true;
    this.invitationToShow = invitation;
  }

  changeAccepted(id: number) {
    this._invitationService.accept(id).subscribe(() => {
      this.dataSource.loadInvitationPage(this.application),
      this.showApply = false;
      this._snackBar.open('Zaproszenie zostało zaakceptowane');

    });
  }

  disableAccept(id: number) {
    this._invitationService.disableAccept(id).subscribe(() => {
      this.dataSource.loadInvitationPage(this.application),
      this.showApply = false;
      this._snackBar.open('Akceptacja zgłoszenia została cofnięta');
    });
  }

  confirm(id: number) {
    this._invitationService.confirm(id).subscribe(() => {
      this.dataSource.loadInvitationPage(this.application),
      this.prepareConfirm(id);
      this.showApply = false;
      this._snackBar.open('Wolontariat został potwierdzony');
    });
  }

  closeApplication(id: number) {
    this.showApply = false;
  }

  prepareConfirm(id: number) {
    this.showConfirmApply = !this.showConfirmApply;
  }
}
