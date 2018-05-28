import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import 'rxjs/add/operator/switchMap';

import { VolunteerRequestVM, VolunteerAdVM } from '../../../shared/interfaces';

@Component({
  selector: 'app-view-ad',
  templateUrl: './view-ad.component.html',
})
export class ViewAdComponent implements OnInit {
  ad: VolunteerAdVM;

  constructor(private _route: ActivatedRoute) {}

  ngOnInit() {
    this._route.data.subscribe((d: { ad: VolunteerAdVM }) => this.ad = d.ad);
  }

}
