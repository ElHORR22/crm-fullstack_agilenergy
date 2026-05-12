import { Pipe, PipeTransform } from '@angular/core';
import { Devis } from '../modeles/devis.model';

@Pipe({
  name: 'filter'
})
export class FilterPipe implements PipeTransform {
  transform(devis: Devis[], searchTerm: string): Devis[] {
    if (!devis || !searchTerm) {
      return devis;
    }
    return devis.filter(devis =>
      devis.sujetDevis.toLowerCase().includes(searchTerm.toLowerCase()) ||
      devis.id?.toString().includes(searchTerm)
    );
  }
}
