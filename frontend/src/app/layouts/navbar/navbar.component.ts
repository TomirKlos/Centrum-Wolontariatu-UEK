import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../shared/auth/auth.service';
import { LoadingBarService } from '../../shared/loading-bar/loading-bar.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss'],
})
export class NavbarComponent implements OnInit {
  displayProgresBar = false;

  constructor(private loadingBarService: LoadingBarService, private authService: AuthService) {
  }

  ngOnInit() {
    this.loadingBarService.display.subscribe(d => this.displayProgresBar = d);
  }

  logout() {
    this.authService.logout();
  }
}
