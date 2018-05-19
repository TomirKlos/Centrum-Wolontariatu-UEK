import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { environment } from '../../../../environments/environment';
import { SnackBarService } from '../../../shared/snack-bar.service';

@Component({
  selector: 'app-activate',
  templateUrl: './activate.component.html',
})
export class ActivateComponent implements OnInit {

  constructor(private http: HttpClient, private route: ActivatedRoute, private router: Router, private sb: SnackBarService) {
  }

  ngOnInit() {
    setTimeout(() => {
      const activationKey = this.route.snapshot.paramMap.get('activation-key');
      this.http.post(environment.apiEndpoint + '/activate', { activationKey }, { observe: 'response' })
        .subscribe(
          ok => {
            this.sb.open('Konto zostało aktywowane', { duration: 3000 });
            this.router.navigateByUrl('/login').then();
          },
          err => {
            this.sb.warning();
            this.router.navigateByUrl('/').then();
          }
        );
    }, 100);
  }

}
