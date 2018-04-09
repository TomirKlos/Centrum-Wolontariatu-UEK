import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from "@angular/router";
import 'rxjs/add/operator/switchMap';

import { VolunteerRequestVM } from "../../shared/interfaces";

@Component({
  selector: 'app-view-request',
  templateUrl: './view-request.component.html',
  styleUrls: [ './view-request.component.scss' ]
})
export class ViewRequestComponent implements OnInit {
  request: VolunteerRequestVM;

  constructor(private _route: ActivatedRoute) {
  }

  ngOnInit() {
    this._route.data.subscribe((d: { request: VolunteerRequestVM }) => this.request = d.request);
  }

}
