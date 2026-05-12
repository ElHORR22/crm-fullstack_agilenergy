import { inject } from '@angular/core';
import { CanActivateFn, Router, ActivatedRouteSnapshot } from '@angular/router';
import { AuthService } from '../service/auth.service';

// ✅ FIX 4 : Migré vers les functional guards (CanActivateFn)
//    CanActivate (class-based) est déprécié depuis Angular 15+
//    Les functional guards sont la norme en Angular 17+

export const authGuard: CanActivateFn = (route: ActivatedRouteSnapshot) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  // ✅ FIX 6 : Utilise authService.isLoggedIn() au lieu de parser localStorage directement
  if (!authService.isLoggedIn()) {
    return router.parseUrl('/login');
  }

  const expectedRoles: string[] = route.data['roles'];
  if (expectedRoles && expectedRoles.length > 0) {
    const userRole = authService.getRole();

    // ✅ FIX 3 (partiel) : On lit quand même depuis localStorage ici,
    //    mais le backend DOIT aussi vérifier le JWT à chaque requête.
    //    Cette vérification côté client est une UX, pas une sécurité réelle.
    if (!userRole || !expectedRoles.includes(userRole)) {
      return router.parseUrl('/unauthorized');
    }
  }

  return true;
};

// ──────────────────────────────────────────────────────────
// COMMENT UTILISER dans app.routes.ts :
//
// import { authGuard } from './guards/auth.guard';
//
// {
//   path: 'clients',
//   component: ClientsComponent,
//   canActivate: [authGuard],
//   data: { roles: ['ADMIN', 'USER'] }
// },
// ──────────────────────────────────────────────────────────
