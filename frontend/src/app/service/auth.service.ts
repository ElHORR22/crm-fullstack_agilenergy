import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Utilisateur } from '../modeles/utilisateur.model';
import { LoginResponse } from '../modeles/loginresponse.model';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private baseUrl = 'http://localhost:8080/api/auth';

  constructor(private http: HttpClient) {}

  login(utilisateur: Utilisateur): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.baseUrl}/login`, utilisateur);
  }
  
  saveSession(res: LoginResponse) {
    localStorage.setItem('token', res.token);
    localStorage.setItem('user', JSON.stringify(res));
  }
    
  getRole(): string | null {
    const userStr = localStorage.getItem('user');
    if (userStr) {
      const user = JSON.parse(userStr);
      return user.role;
    }
    return null;
  } 
  
  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    localStorage.removeItem('role');
  }  
  
}
