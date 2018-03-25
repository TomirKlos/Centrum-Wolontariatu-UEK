import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, NavigationEnd, Router } from '@angular/router';
import 'rxjs/add/operator/first';
import 'rxjs/add/operator/filter';

@Component({
  selector: 'app-account',
  templateUrl: './account.component.html',
  styleUrls: ['./account.component.scss']
})
export class AccountComponent implements OnInit {
  title: string;

  constructor(private route: ActivatedRoute, private router: Router) {
  }

  ngOnInit() {
    this.getTitle();
    this.router.events
      .filter(e => e instanceof NavigationEnd)
      .subscribe(d => this.getTitle());
  }

  private getTitle() {
    this.route.firstChild.data
      .first()
      .subscribe(d => this.title = d.title);
  }

}