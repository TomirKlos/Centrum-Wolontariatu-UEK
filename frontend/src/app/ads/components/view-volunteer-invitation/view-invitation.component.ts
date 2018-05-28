import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import 'rxjs/add/operator/switchMap';
import { InvitationVolunteerRequestVM } from '../../../shared/interfaces';

@Component({
  selector: 'app-view-invitation',
  templateUrl: './view-invitation.component.html',
})
export class ViewApplyRequestComponent implements OnInit {
  application: InvitationVolunteerRequestVM;
  

  constructor(private _route: ActivatedRoute) {}

  ngOnInit() {
    this._route.data.subscribe((d: { application: InvitationVolunteerRequestVM }) => this.application = d.application);
  }

}
