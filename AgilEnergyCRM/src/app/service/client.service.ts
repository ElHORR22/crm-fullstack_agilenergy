import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Client, Gouvernorat, SecteurActivite } from '../modeles/client.model';

@Injectable({
  providedIn: 'root'
})
export class ClientService {

  private apiUrl = 'http://localhost:8080/api/clients';  

  constructor(private http: HttpClient) { }

  getAllClients(): Observable<Client[]> {
    return this.http.get<Client[]>(this.apiUrl);
  }

  getGouvernorats(): Observable<Gouvernorat[]> {
    return this.http.get<Gouvernorat[]>('http://localhost:8080/api/gouvernorats');
  }

  getSecteurActivite(): Observable<SecteurActivite[]> {
    return this.http.get<SecteurActivite[]>('http://localhost:8080/api/secteurs');
  }
  
  addClient(client: Client): Observable<Client> {
    return this.http.post<Client>('http://localhost:8080/api/clients', client);
  }   
  
  updateClient(client: Client): Observable<Client> {
    return this.http.put<Client>(`http://localhost:8080/api/clients/${client.id}`, client);
  }  
  
  deleteClient(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
