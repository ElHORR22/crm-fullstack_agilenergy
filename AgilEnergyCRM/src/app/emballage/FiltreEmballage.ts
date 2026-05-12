import { Pipe, PipeTransform } from '@angular/core';
import { Emballage } from '../modeles/emballage.model';

@Pipe({
  name: 'filtre',
  standalone: true
})
export class FiltrePipe implements PipeTransform {
  transform(emballages: Emballage[], searchTerm: string): Emballage[] {
    if (!emballages || !searchTerm) {
      return emballages;
    }
    return emballages.filter(emballage =>
      emballage.codeEmballage?.toString().includes(searchTerm) ||
      emballage.id?.toString().includes(searchTerm)
    );
  }
}
