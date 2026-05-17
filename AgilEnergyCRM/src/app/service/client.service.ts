import { environment } from '../../environments/environment';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Client, Gouvernorat, SecteurActivite } from '../modeles/client.model';

@Injectable({
  providedIn: 'root'
})
export class ClientService {

  private apiUrl = `${environment.apiUrl}/api/clients`;  

  constructor(private http: HttpClient) { }

  getAllClients(): Observable<Client[]> {
    return this.http.get<Client[]>(this.apiUrl);
  }

  getGouvernorats(): Observable<Gouvernorat[]> {
    return this.http.get<Gouvernorat[]>(`${environment.apiUrl}/api/gouvernorats`);
  }

  getSecteurActivite(): Observable<SecteurActivite[]> {
    return this.http.get<SecteurActivite[]>(`${environment.apiUrl}/api/secteurs`);
  }
  
  addClient(client: Client): Observable<Client> {
    return this.http.post<Client>(`${environment.apiUrl}/api/clients`, client);
  }   
  
  updateClient(client: Client): Observable<Client> {
    return this.http.put<Client>(`${environment.apiUrl}/api/clients/${client.id}`, client);
  }  
  
  deleteClient(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
