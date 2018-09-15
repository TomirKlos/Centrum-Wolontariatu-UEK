import { OnInit, Component, Inject } from "@angular/core";
import { FormGroup, FormBuilder } from "@angular/forms";
import { VolunteerRequestVM, Page } from "../../../shared/interfaces";
import { MatDialogRef, MAT_DIALOG_DATA } from "@angular/material";
import { RequestService } from "../../../requests/shared/request.service";

@Component({
    selector: 'invite-choose-request-dialog',
    templateUrl: 'invite-ad-dialog.component.html',
  })
  export class InviteChooseRequestDialogComponent implements OnInit {

    formGroup: FormGroup;
    categoriesData: String[] = [];
    requestsData: VolunteerRequestVM[] = [];
    chosenTitle: string;

    constructor(
      public dialogRef: MatDialogRef<InviteChooseRequestDialogComponent>,
      private _fb: FormBuilder,
      public _requestService: RequestService,
      @Inject(MAT_DIALOG_DATA) public data: any) { }

      ngOnInit(){
        this._requestService.getMine()
        .subscribe((data: Page<VolunteerRequestVM>) => {
          data.content.forEach(element => {
            this.requestsData.push(element);
          });
        });

        this.formGroup = this._fb.group({
          vrequest: [ ],
        });
      }

    onNoClick(): void {
      this.dialogRef.close();
    }

    setChosen(s: string){
      this.chosenTitle = s;
    }

  }
