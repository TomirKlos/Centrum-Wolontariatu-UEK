import { Directive, Input, TemplateRef, ViewContainerRef } from '@angular/core';
import { AuthService } from './auth.service';
import { UserService } from './user.service';

@Directive({
  selector: '[appHasAnyRole]'
})
export class HasAnyRoleDirective {
  private _roles: string[];

  constructor(private templateRef: TemplateRef<any>,
              private viewContainerRef: ViewContainerRef,
              private authService: AuthService,
              private userService: UserService) {
  }

  @Input()
  set appHasAnyRole(value: string[]) {
    this._roles = value;

    this._updateView();
    // Get notified each time authentication state changes.
    this.userService.roles$.subscribe(() => this._updateView());
  }

  private _updateView(): void {
    const result = this.userService.hasAnyRole(this._roles);
    this.viewContainerRef.clear();
    if (result) {
      this.viewContainerRef.createEmbeddedView(this.templateRef);
    }
  }
}
