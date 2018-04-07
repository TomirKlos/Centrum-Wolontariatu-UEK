import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { SnackBarService } from '../shared/snack-bar.service';

@Component({
  selector: 'app-dump',
  templateUrl: './dump.component.html',
  styleUrls: ['./dump.component.scss']
})
export class DumpComponent implements OnInit {

  constructor(private http: HttpClient, private _sb: SnackBarService) {
  }

  ngOnInit() {
    this.http.get(environment.apiEndpoint + '/account').subscribe(d => {
        console.log(d);
      },
      e => console.log(e));
  }

  opena() {
    this._sb.open('a');
  }

  openErr() {
    this._sb.warning('a');
  }

}
