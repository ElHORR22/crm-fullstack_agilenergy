import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { SecteurActivite, SourceProspection, Gouvernorat, Prospect } from '../modeles/prospect.model';
import { Client } from '../modeles/client.model';

@Injectable({
  providedIn: 'root'
})
export class ProspectService {

  private apiUrl = `${environment.apiUrl}/api/prospects`;  

  constructor(private http: HttpClient) { }

  getAllProspects(): Observable<Prospect[]> {
    return this.http.get<Prospect[]>(`${environment.apiUrl}/api/prospects`);
  }

  getGouvernorats(): Observable<Gouvernorat[]> {
    return this.http.get<Gouvernorat[]>(`${environment.apiUrl}/api/gouvernorats`);
  }

  getSourceProspection(): Observable<SourceProspection[]> {
    return this.http.get<SourceProspection[]>(`${environment.apiUrl}/api/sourcesprospection`);
  }

  getSecteurActivite(): Observable<SecteurActivite[]> {
    return this.http.get<SecteurActivite[]>(`${environment.apiUrl}/api/secteurs`);
  }
  
  addProspect(prospect: Prospect): Observable<Prospect> {
    return this.http.post<Prospect>(`${environment.apiUrl}/api/prospects`, prospect);
  }  
  
  updateProspect(prospect: Prospect): Observable<Prospect> {
    return this.http.put<Prospect>(`${environment.apiUrl}/api/prospects/${prospect.id}`, prospect);
  }  
  
  deleteProspect(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  convertirEnClient(prospect: Prospect): Observable<Client> {
    return this.http.post<Client>(`${this.apiUrl}/convertir`, prospect);
  }  
  
}
