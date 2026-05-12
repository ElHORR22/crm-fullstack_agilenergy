import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Utilisateur } from '../modeles/utilisateur.model';
import { LoginResponse } from '../modeles/loginresponse.model';
import { environment } from '../environments/environment';

@Injectable({ providedIn: 'root' })
export class AuthService {

  // ✅ FIX 1 : URL centralisée dans environment.ts — plus de localhost hardcodé
  private baseUrl = `${environment.apiUrl}/api/auth`;

  constructor(private http: HttpClient) { }

  login(utilisateur: Utilisateur): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.baseUrl}/login`, utilisateur);
  }

  saveSession(res: LoginResponse): void {
    localStorage.setItem('token', res.token);
    localStorage.setItem('user', JSON.stringify(res));
  }

  // ✅ FIX 6 : Méthode isLoggedIn ajoutée — utilisée par le guard
  isLoggedIn(): boolean {
    return !!localStorage.getItem('token');
  }

  // ✅ FIX 6 : Méthode getToken pour l'interceptor
  getToken(): string | null {
    return localStorage.getItem('token');
  }

  getRole(): string | null {
    const userStr = localStorage.getItem('user');
    if (userStr) {
      try {
        const user = JSON.parse(userStr);
        return user.role ?? null;
      } catch {
        return null;
      }
    }
    return null;
  }

  // ✅ FIX 9 : Suppression du removeItem('role') inutile (n'a jamais été stocké séparément)
  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
  }

  requestPasswordReset(email: string): Observable<any> {
    return this.http.post(`${this.baseUrl}/forgot-password`, { email });
  }

  resetPassword(token: string, password: string): Observable<any> {
    return this.http.post(`${this.baseUrl}/reset-password`, { token, password });
  }
}
