import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { HttpClient } from "@angular/common/http";
import { Emballage } from "../modeles/emballage.model";

@Injectable({
    providedIn: 'root'
})
export class EmballageService {
    private apiUrl = 'http://localhost:8080/api/emballages';
  
    constructor(private http: HttpClient) {}
  
    getAllEmballages(): Observable<Emballage[]> {
        return this.http.get<Emballage[]>(this.apiUrl);
    }      
  
    addEmballage(emballage: any): Observable<Emballage> {
      return this.http.post<Emballage>('http://localhost:8080/api/emballages', emballage);
    }
    
    updateEmballage(emballage: Emballage): Observable<Emballage> {
      return this.http.put<Emballage>(`${this.apiUrl}/${emballage.id}`, emballage);
    }         
  
    deleteEmballage(id: number): Observable<any> {
      return this.http.delete(`${this.apiUrl}/${id}`);
    }    
}