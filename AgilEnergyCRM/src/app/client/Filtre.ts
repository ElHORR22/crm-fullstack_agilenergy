import { Pipe, PipeTransform } from '@angular/core';
import { Client } from '../modeles/client.model';

@Pipe({
  name: 'filter'
})
export class FilterPipe implements PipeTransform {
  transform(clients: Client[], searchTerm: string): Client[] {
    if (!clients || !searchTerm) {
      return clients;
    }
    return clients.filter(client =>
      // nom/email peuvent être null en base : ?? '' évite une erreur qui viderait la liste.
      (client.nom ?? '').toLowerCase().includes(searchTerm.toLowerCase()) ||
      (client.email ?? '').toLowerCase().includes(searchTerm.toLowerCase()) ||
      (client.telephone ?? '').toString().includes(searchTerm) ||
      client.id?.toString().includes(searchTerm)
    );
  }
}
