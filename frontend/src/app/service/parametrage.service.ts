import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Tva, SourceProspection, SecteurActivite } from '../modeles/parametrage.model';

@Injectable({
  providedIn: 'root'
})
export class ParametrageService {
  private baseUrl = 'http://localhost:8080/api/parametrage';

  constructor(private http: HttpClient) {}

  getAllTVA(): Observable<Tva[]> {
    return this.http.get<Tva[]>('http://localhost:8080/api/parametrage/tva');
  }  

  updateTva(updatedTva: number): Observable<Tva> {
    const url = 'http://localhost:8080/api/parametrage/tva'; // Assure-toi que l'URL est correcte
    return this.http.put<Tva>(url, { valeur: updatedTva });
  }  

  getSources(): Observable<SourceProspection[]> {
    return this.http.get<SourceProspection[]>(`${this.baseUrl}/sources`);
  }

  addSource(source: SourceProspection): Observable<SourceProspection> {
    return this.http.post<SourceProspection>(`${this.baseUrl}/sources`, source);
  }

  deleteSource(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/sources/${id}`);
  }

  getSecteurs(): Observable<SecteurActivite[]> {
    return this.http.get<SecteurActivite[]>(`${this.baseUrl}/secteurs`);
  }

  addSecteur(secteur: SecteurActivite): Observable<SecteurActivite> {
    return this.http.post<SecteurActivite>(`${this.baseUrl}/secteurs`, secteur);
  }

  deleteSecteur(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/secteurs/${id}`);
  }
}
