import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Devis } from '../modeles/devis.model';
import { LigneDevis } from '../modeles/lignedevis.model';

@Injectable({
  providedIn: 'root'
})
export class DevisService {

  private baseUrl = 'http://localhost:8080/api/devis';
  private ligneUrl = 'http://localhost:8080/api/lignes-devis';

  constructor(private http: HttpClient) { }

  getTVA(): Observable<number> {
    return this.http.get<number>(`${this.baseUrl}/tva`);
  }  

  getAllDevis(): Observable<Devis[]> {
    return this.http.get<Devis[]>(this.baseUrl);
  }

  getDevisById(id: number): Observable<Devis> {
    return this.http.get<Devis>(`${this.baseUrl}/${id}`);
  }

  getDevisByEmail(email: string): Observable<Devis[]> {
    return this.http.get<Devis[]>(`${this.baseUrl}/par-email?email=${encodeURIComponent(email)}`);
  }  

  addDevis(devis: Devis): Observable<Devis> {
    return this.http.post<Devis>(this.baseUrl, devis);
  }

  updateDevis(id: number, devis: Devis): Observable<Devis> {
    return this.http.put<Devis>(`${this.baseUrl}/${id}`, devis);
  }

  deleteDevis(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }

  getLignesByDevisId(devisId: number): Observable<LigneDevis[]> {
    return this.http.get<LigneDevis[]>(`${this.ligneUrl}/by-devis/${devisId}`);
  }

  addLigneDevis(ligne: LigneDevis): Observable<LigneDevis> {
    return this.http.post<LigneDevis>(this.ligneUrl, ligne);
  }

  updateLigneDevis(id: number, ligne: LigneDevis): Observable<LigneDevis> {
    return this.http.put<LigneDevis>(`${this.ligneUrl}/${id}`, ligne);
  }

  deleteLigneDevis(id: number): Observable<void> {
    return this.http.delete<void>(`${this.ligneUrl}/${id}`);
  }

  exportPDF(devisId: number): void {
    window.open(`http://localhost:8080/api/devis/${devisId}/pdf`, '_blank');
  }
  
}
