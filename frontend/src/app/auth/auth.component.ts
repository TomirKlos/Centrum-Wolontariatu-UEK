import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-auth',
  templateUrl: './auth.component.html',
  styleUrls: ['./auth.component.scss']
})
export class AuthComponent implements OnInit {
  title: string;

  constructor(private route: ActivatedRoute) {
  }

  ngOnInit() {
    this.route.firstChild.data.subscribe(d => this.title = d.title);
  }

}
