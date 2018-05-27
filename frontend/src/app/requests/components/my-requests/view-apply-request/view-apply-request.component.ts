import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import 'rxjs/add/operator/switchMap';

import { VolunteerRequestVM, responseVolunteerRequestVM } from '../../../../shared/interfaces';

@Component({
  selector: 'app-apply-view-request',
  templateUrl: './view-apply-request.component.html',
})
export class ViewApplyRequestComponent implements OnInit {
  application: responseVolunteerRequestVM;
  

  constructor(private _route: ActivatedRoute) {}

  ngOnInit() {
    this._route.data.subscribe((d: { application: responseVolunteerRequestVM }) => this.application = d.application);
  }

}
