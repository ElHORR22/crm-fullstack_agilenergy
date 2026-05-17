import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Devis } from '../modeles/devis.model';
import { LigneDevis } from '../modeles/lignedevis.model';
import { environment } from '../environments/environment';

@Injectable({ providedIn: 'root` })
export class DevisService {

  // ✅ FIX 1 : URLs centralisées via environment.ts
  private baseUrl = `${environment.apiUrl}/api/devis`;
  private ligneUrl = `${environment.apiUrl}/api/lignes-devis`;

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

  // ✅ FIX 7 : Le PDF est téléchargé via HttpClient (avec JWT dans le header grâce à l`interceptor)
  // au lieu de window.open() qui ne peut pas envoyer de header Authorization
  exportPDF(devisId: number): Observable<Blob> {
    return this.http.get(`${this.baseUrl}/${devisId}/pdf`, {
      responseType: 'blob'
    });
  }

  // Méthode utilitaire pour déclencher le téléchargement du PDF dans le navigateur
  downloadPDF(devisId: number, nomFichier: string = `devis-${devisId}.pdf`): void {
    this.exportPDF(devisId).subscribe(blob => {
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = nomFichier;
      a.click();
      window.URL.revokeObjectURL(url);
    });
  }
}
