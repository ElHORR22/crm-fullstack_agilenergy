import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';
import { AuthService } from '../services/auth.service';

// ✅ FIX 4 & 5 : Migré vers functional interceptor (norme Angular 17+)
//    + localStorage.clear() remplacé par logout() ciblé
//    + Gestion d'erreurs centralisée (FIX 10)

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  // ✅ Utilise authService.getToken() au lieu de lire directement localStorage
  const token = authService.getToken();

  const authReq = token
    ? req.clone({ setHeaders: { Authorization: `Bearer ${token}` } })
    : req;

  return next(authReq).pipe(
    catchError((error: HttpErrorResponse) => {
      if (error.status === 401 || error.status === 403) {
        // ✅ FIX 5 : logout() ciblé — ne supprime que token et user,
        //    pas toutes les données localStorage (anciennement localStorage.clear())
        authService.logout();
        router.navigate(['/login']);
      }
      // ✅ FIX 10 : Les erreurs remontent correctement pour traitement par les composants
      return throwError(() => error);
    })
  );
};

// ──────────────────────────────────────────────────────────
// COMMENT UTILISER dans app.config.ts :
//
// import { provideHttpClient, withInterceptors } from '@angular/common/http';
// import { authInterceptor } from './interceptors/auth.interceptor';
//
// export const appConfig: ApplicationConfig = {
//   providers: [
//     provideHttpClient(withInterceptors([authInterceptor])),
//     ...
//   ]
// };
// ──────────────────────────────────────────────────────────
