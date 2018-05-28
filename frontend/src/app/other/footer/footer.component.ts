import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../shared/auth/auth.service';
import { LoadingBarService } from '../../shared/loading-bar/loading-bar.service';

@Component({
  selector: 'app-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.scss'],
})
export class FooterComponent implements OnInit {

  constructor(private loadingBarService: LoadingBarService) {
  }

  ngOnInit() {

  }
}
