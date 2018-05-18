import { Component } from '@angular/core';
import 'rxjs/add/operator/first';
import 'rxjs/add/operator/filter';

@Component({
  selector: 'app-account',
  styles: [ '#container { width: 100%; max-width: 400px;}' ],
  template: `
    <div fxLayout="row" fxLayoutAlign="center">
      <div id="container">
        <router-outlet></router-outlet>
      </div>
    </div>
  `,
})
export class AccountComponent {

}
