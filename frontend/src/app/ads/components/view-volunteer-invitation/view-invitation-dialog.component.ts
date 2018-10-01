import {AfterViewInit, Component, Inject, OnInit, ViewChild} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef, MatSort, MatPaginator, MatSortable, MatDialogConfig} from '@angular/material';
import {DialogData, InvitationVolunteerRequestVM, Page, responseVolunteerRequestVM, VolunteerRequestVM} from '../../../shared/interfaces';
import { ServerDataSource } from '../../../shared/server-data-source';
import { InvitationService } from './invitation.service';
import { SnackBarService } from '../../../shared/snack-bar.service';
import {tap} from 'rxjs/operators';
import {merge} from 'rxjs/observable/merge';
import {RequestDialogService} from '../../../requests/shared/request-dialog.service';
import {RequestService} from '../../../requests/shared/request.service';
import {Observable} from 'rxjs/Observable';


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
    columnsToDisplay = [ 'id', 'accepted', 'description', 'volunteerRequest', 'showApply' ];

    @ViewChild(MatPaginator) paginator: MatPaginator;
    @ViewChild(MatSort) sort: MatSort;


  constructor(
    public dialogRef: MatDialogRef<ViewInvitationDialogComponent>,
    private _invitationService: InvitationService,
    public _snackBar: SnackBarService,
    private _dialogService: RequestDialogService,
    private _requestService: RequestService,

    @Inject(MAT_DIALOG_DATA) public data: number) {
      console.log(data.valueOf)
    this.application = data;

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

  closeApplication(id: number) {
    this.showApply = false;
  }

  prepareConfirm(id: number) {
    this.showConfirmApply = !this.showConfirmApply;
  }

  openVolunteerRequest(data: number){
    this._requestService.getById(data).subscribe((d: Page<VolunteerRequestVM>) => {
      let dialogData = new DialogData();
      dialogData.showApplyButton = false;
      dialogData.volunteerRequest = d.content[0]

      this._dialogService.open(dialogData);
    });
  }

}
