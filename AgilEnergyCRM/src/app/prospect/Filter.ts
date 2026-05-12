import { Pipe, PipeTransform } from '@angular/core';
import { Prospect } from '../modeles/prospect.model';

@Pipe({
  name: 'filter'
})
export class FilterPipe implements PipeTransform {
  transform(prospect: Prospect[], searchTerm: string): Prospect[] {
    if (!prospect || !searchTerm) {
      return prospect;
    }
    return prospect.filter(prospect =>
      prospect.nom.toLowerCase().includes(searchTerm.toLowerCase()) ||
      prospect.email.toLowerCase().includes(searchTerm.toLowerCase()) ||
      prospect.telephone.toString().includes(searchTerm) ||
      prospect.id!.toString().includes(searchTerm)
    );
  }
}
