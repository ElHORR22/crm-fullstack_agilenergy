import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { SecteurActivite, SourceProspection, Gouvernorat, Prospect } from '../modeles/prospect.model';
import { Client } from '../modeles/client.model';

@Injectable({
  providedIn: 'root'
})
export class ProspectService {

  private apiUrl = 'http://localhost:8080/api/prospects';  

  constructor(private http: HttpClient) { }

  getAllProspects(): Observable<Prospect[]> {
    return this.http.get<Prospect[]>('http://localhost:8080/api/prospects');
  }

  getGouvernorats(): Observable<Gouvernorat[]> {
    return this.http.get<Gouvernorat[]>('http://localhost:8080/api/gouvernorats');
  }

  getSourceProspection(): Observable<SourceProspection[]> {
    return this.http.get<SourceProspection[]>('http://localhost:8080/api/sourcesprospection');
  }

  getSecteurActivite(): Observable<SecteurActivite[]> {
    return this.http.get<SecteurActivite[]>('http://localhost:8080/api/secteurs');
  }
  
  addProspect(prospect: Prospect): Observable<Prospect> {
    return this.http.post<Prospect>('http://localhost:8080/api/prospects', prospect);
  }  
  
  updateProspect(prospect: Prospect): Observable<Prospect> {
    return this.http.put<Prospect>(`http://localhost:8080/api/prospects/${prospect.id}`, prospect);
  }  
  
  deleteProspect(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  convertirEnClient(prospect: Prospect): Observable<Client> {
    return this.http.post<Client>(`${this.apiUrl}/convertir`, prospect);
  }  
  
}
