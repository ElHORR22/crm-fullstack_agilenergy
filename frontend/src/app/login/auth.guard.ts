import { Injectable } from '@angular/core';
import { CanActivate, Router, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  constructor(private router: Router) {}

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): boolean | UrlTree {
    const userStr = localStorage.getItem('user');
    if (!userStr) {
      return this.router.parseUrl('/login');
    }

    const user = JSON.parse(userStr);
    const expectedRoles: string[] = route.data['roles'];

    if (expectedRoles && !expectedRoles.includes(user.role)) {
      return this.router.parseUrl('/unauthorized'); 
    }

    return true;
  }
}
