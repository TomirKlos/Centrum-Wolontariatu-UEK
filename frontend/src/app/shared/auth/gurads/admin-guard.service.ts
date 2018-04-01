import { Injectable } from '@angular/core';
import { UserService } from '../user.service';
import { ActivatedRouteSnapshot, CanActivate, CanActivateChild, Router, RouterStateSnapshot } from '@angular/router';
import { AuthService } from '../auth.service';

@Injectable()
export class AdminGuardService implements CanActivate, CanActivateChild {

  constructor(private _authService: AuthService, private _router: Router, private _userService: UserService) {
  }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
    return this.checkLogin();
  }

  canActivateChild(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
    return this.canActivate(route, state);
  }

  private checkLogin(): boolean {
    if (this._userService.isLoggedIn) {
      return this._userService.hasAnyRole(['ROLE_ADMIN']);
    } else {
      this._router.navigate(['/']).then();
      return false;
    }
  }

}
