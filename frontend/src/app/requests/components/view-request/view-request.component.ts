import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import 'rxjs/add/operator/switchMap';

import { VolunteerRequestVM } from '../../../shared/interfaces';

@Component({
  selector: 'app-view-request',
  templateUrl: './view-request.component.html',
})
export class ViewRequestComponent implements OnInit {
  request: VolunteerRequestVM;
  viewApplyForm: boolean = false;

  constructor(private _route: ActivatedRoute) {}

  ngOnInit() {
    this._route.data.subscribe((d: { request: VolunteerRequestVM }) => this.request = d.request);
  }

  replaceLineBreak(s:string) {
    return s && s.replace(/\n/g,' <br /> ');
  }

  showApplyForm(){
    this.viewApplyForm = !this.viewApplyForm;
  }
}
