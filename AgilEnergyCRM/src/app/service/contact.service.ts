import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { Contact } from "../modeles/contact.model";
import { HttpClient } from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class ContactService {
  private apiUrl = 'http://localhost:8080/api/contacts';

  constructor(private http: HttpClient) { }

  getContactsByClientId(clientId: number): Observable<Contact[]> {
    return this.http.get<Contact[]>(`http://localhost:8080/api/contacts/client/${clientId}`);
  }

  addContact(contact: any): Observable<Contact> {
    return this.http.post<Contact>('http://localhost:8080/api/contacts', contact);
  }

  updateContact(id: number, contact: Contact): Observable<any> {
    return this.http.put(`http://localhost:8080/api/contacts/${id}`, contact);
  }  

  deleteContact(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}