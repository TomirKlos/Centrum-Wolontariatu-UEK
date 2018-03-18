import { Component, OnInit } from '@angular/core';
import { LoadingBarService } from './shared/loading-bar/loading-bar.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  displayProgresBar = false;

  constructor(private loadingBarService: LoadingBarService) {
  }

  ngOnInit() {
    this.loadingBarService.display.subscribe(d => this.displayProgresBar = d);
  }
}
