import { HttpClient, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";

export interface MoisDevis {
  mois: string;
  nbDevis: number;
}

export interface ProduitDemandes {
  codeProduit: string;
  quantite: number;
}

export interface ClientsProspectsCount {
  clients: number;
  prospects: number;
}

@Injectable({ providedIn: 'root' })
export class DashboardService {
  private baseUrl = 'http://localhost:8080/api/dashboard';

  constructor(private http: HttpClient) {}

  getChiffreAffaires(): Observable<number> {
    return this.http.get<number>(`${this.baseUrl}/chiffre-affaires`);
  }

  getClientsParGouvernorat(): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/clients-par-gouvernorat`);
  }

  getClientsParSecteur(): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/clients-par-secteur`);
  }

  getCaMensuel(annee: number, mois: number): Observable<number> {
    const params = new HttpParams()
      .set('annee', annee.toString())
      .set('mois', mois.toString());
    return this.http.get<number>(`${this.baseUrl}/ca-mensuel`, { params });
  }

  getCaDernierMois() {
    return this.http.get<{ mois: number; ca: number }[]>('/api/devis/chiffre-affaires-dernier-mois');
  }

  getChiffreAffairesParMois(mois: number, annee: number): Observable<number> {
    return this.http.get<number>(`${this.baseUrl}/chiffre-affaires?mois=${mois}&annee=${annee}`);
  }

  getDevisParMois(): Observable<MoisDevis[]> {
    return this.http.get<MoisDevis[]>(`${this.baseUrl}/devis-par-mois`);
  }

  getTopProduits(): Observable<ProduitDemandes[]> {
    return this.http.get<ProduitDemandes[]>(`${this.baseUrl}/produits-top5`);
  }

  getClientsProspectsCount(): Observable<ClientsProspectsCount> {
    return this.http.get<ClientsProspectsCount>(`${this.baseUrl}/clients-prospects-count`);
  }
  
}

