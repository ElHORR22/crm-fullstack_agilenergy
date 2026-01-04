import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { HttpClient } from "@angular/common/http";
import { Emballage } from "../modeles/emballage.model";
import { Produit } from "../modeles/produit.model";

@Injectable({
    providedIn: 'root'
})
export class ProduitService {
    private apiUrl = 'http://localhost:8080/api/produits';
  
    constructor(private http: HttpClient) {}
  
    getAllProduits(): Observable<Produit[]> {
      return this.http.get<Produit[]>(this.apiUrl);
    }      

    getEmballages(): Observable<Emballage[]> {
      return this.http.get<Emballage[]>('http://localhost:8080/api/emballages');
    }
  
    addProduit(produit: any): Observable<Produit> {
      return this.http.post<Produit>('http://localhost:8080/api/produits', produit);
    }
    
    updateProduit(produit: Produit): Observable<Produit> {
      return this.http.put<Produit>(`${this.apiUrl}/${produit.id}`, produit);
    }         
  
    deleteProduit(id: number): Observable<any> {
      return this.http.delete(`${this.apiUrl}/${id}`);
    }    
}