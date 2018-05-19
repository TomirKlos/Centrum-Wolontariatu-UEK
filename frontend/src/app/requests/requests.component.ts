import { Component } from '@angular/core';

@Component({
  selector: 'app-requests',
  styles: [ '#container { width: 100%; max-width: 900px;}' ],
  template: `
    <div fxLayout="row" fxLayoutAlign="center">
      <div id="container">
        <router-outlet></router-outlet>
      </div>
    </div>
  `,
})
export class RequestsComponent {

  constructor() {
  }

}
