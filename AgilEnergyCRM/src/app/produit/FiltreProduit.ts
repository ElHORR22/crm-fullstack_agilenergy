import { Pipe, PipeTransform } from '@angular/core';
import { Produit } from '../modeles/produit.model';

@Pipe({
  name: 'filtreproduit',
  standalone: true
})
export class FiltrePipe implements PipeTransform {
  transform(produits: Produit[], searchTerm: string): Produit[] {
    if (!produits || !searchTerm) {
      return produits;
    }
    return produits.filter(produit =>
        produit.libelleProduit.toLowerCase().includes(searchTerm.toLowerCase()) ||
        produit.codeProduit?.toString().includes(searchTerm) ||
        produit.id?.toString().includes(searchTerm)
    );
  }
}
