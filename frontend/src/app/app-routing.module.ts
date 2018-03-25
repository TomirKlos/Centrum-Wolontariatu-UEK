import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { DumpComponent } from './dump/dump.component';

const routes: Routes = [
  {
    path: '',
    component: HomeComponent,
  },
  {
    path: 'dump',
    component: DumpComponent,
    // canActivate: [AuthGuardService]
  }
];

@NgModule({
  imports: [
    RouterModule.forRoot(routes, { enableTracing: false }) // <-- debugging purposes only
  ],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
