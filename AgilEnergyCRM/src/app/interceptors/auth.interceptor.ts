import { Injectable } from '@angular/core';
import {
  HttpEvent, HttpHandler, HttpInterceptor,
  HttpRequest, HttpErrorResponse
} from '@angular/common/http';
import { Observable, catchError, throwError } from 'rxjs';
import { Router } from '@angular/router';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  constructor(private router: Router) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

    const token = localStorage.getItem('token');

    let authReq = req;
    if (token) {
      authReq = req.clone({
        setHeaders: { Authorization: `Bearer ${token}` }
      });
    }

    return next.handle(authReq).pipe(
      catchError((error: HttpErrorResponse) => {
        // Les erreurs de /api/auth (mauvais mot de passe, lien expiré…) sont gérées par les composants.
        if (!req.url.includes('/api/auth/')) {
          if (error.status === 401) {
            // Session absente ou expirée, ou compte désactivé : retour à la connexion.
            localStorage.clear();
            this.router.navigate(['/login']);
          } else if (error.status === 403) {
            // Connecté mais rôle insuffisant : la session reste valide.
            this.router.navigate(['/nonautorise']);
          }
        }
        return throwError(() => error);
      })
    );
  }
}