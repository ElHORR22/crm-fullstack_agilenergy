import { Component, HostListener, OnInit } from '@angular/core';
import { Produit } from '../modeles/produit.model';
import { ProduitService } from '../service/produit.service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { FiltrePipe } from './FiltreProduit';
import { Router } from '@angular/router';

@Component({
  selector: 'app-produit',
  standalone: true,
  templateUrl: './produit.component.html',
  styleUrl: './produit.component.css',
  imports: [FormsModule, CommonModule, FiltrePipe]
})
export class ProduitComponent implements OnInit {

  role: string | null = null;
  openedProduit: any;
  progressValue = 0;
  produits: Produit[] = [];
  filteredProduits: Produit[] = [];
  searchTerm: string = '';
  errorMessage: string = '';
  successMessage: string = '';
  selectedProduits: Produit[] = [];
  selectedProduit: Produit | null = null;
  newProduit: Produit = new Produit();
  showAddForm: boolean = false;
  showEditForm: boolean = false;
  emballages: any[] = [];
  showErrorPopup: boolean = false;
  showDetailPopup: boolean = false;

  constructor(private produitService: ProduitService, private router: Router) { }

  ngOnInit() {
    this.loadProduits();
    this.getEmballages();
  }

  updateProgress() {
    let filled = 0;
    const total = 12;

    const p = this.newProduit;
    if (p.codeProduit) filled++;
    if (p.libelleProduit) filled++;
    if (p.poidsProduit) filled++;
    if (p.typeProduit) filled++;
    if (p.codeEmballage) filled++;
    if (p.categorie) filled++;
    if (p.actif) filled++;
    if (p.ecozit) filled++;
    if (p.packageProduit) filled++;
    if (p.prixGros) filled++;
    if (p.prixGerant) filled++;
    if (p.prixDetail) filled++;

    this.progressValue = Math.floor((filled / total) * 100);
  }

  loadProduits(): void {
    this.produitService.getAllProduits().subscribe({
      next: (produits) => {

        this.produits = produits.filter(produit => !produit.isDeleted);
        this.filteredProduits = [...this.produits];
      }
    });
  }

  getEmballages() {
    this.produitService.getEmballages().subscribe({
      next: (data) => {
        console.log("Emballages récupérés:", data);
        this.emballages = data;
      },
      error: (err) => {
        console.error("Erreur lors du chargement des emballages", err);
      }
    });
  }

  searchProduits(): void {
    if (this.searchTerm) {
      this.filteredProduits = this.produits.filter(produit =>
        produit.codeProduit.toString().includes(this.searchTerm) ||
        produit.id?.toString().includes(this.searchTerm)
      );
    } else {
      this.filteredProduits = this.produits;
    }
  }

  onProduitSelect(produit: Produit, event: any): void {
    const checked = event.target.checked;
    this.filteredProduits.forEach(p => {
      p['checked'] = false;
      p['showDropdown'] = false;
    });

    if (checked) {
      produit['checked'] = true;
      this.selectedProduit = produit;
    } else {
      this.selectedProduit = null;
    }
  }

  isSelected(produit: Produit): boolean {
    return this.selectedProduits.some(p => p.id === produit.id);
  }

  toggleAddForm(): void {
    this.showAddForm = !this.showAddForm;
    this.newProduit = new Produit();
  }

  openPopup(produit: Produit): void {
    this.selectedProduit = produit;
    this.showDetailPopup = true;
  }

  closePopup(): void {
    this.selectedProduit = null;
    this.showDetailPopup = false;
  }

  closeErrorPopup(): void {
    this.showErrorPopup = false;
    this.errorMessage = '';
  }

  addProduit(): void {
    console.log("Code d'Emballage sélectionné:", this.newProduit.codeEmballage);

    if (!this.newProduit.codeProduit || !this.newProduit.libelleProduit || !this.newProduit.poidsProduit || !this.newProduit.codeEmballage ||
      !this.newProduit.categorie || !this.newProduit.actif || !this.newProduit.packageProduit || !this.newProduit.typeProduit || !this.newProduit.ecozit || !this.newProduit.prixGros || !this.newProduit.prixDetail || !this.newProduit.prixGerant) {
      this.errorMessage = 'Tous les champs obligatoires doivent être remplis !';
      return;
    }

    const codeRegex = /^\d+$/;
    if (!codeRegex.test(this.newProduit.codeEmballage.toString())) {
      this.errorMessage = 'Le code doit contenir uniquement des chiffres !';
      return;
    }

    const prixValide = (prix: any) =>
      typeof prix === 'number' && prix > 0 && /^\d+(\.\d+)?$/.test(prix.toString());

    if (
      !prixValide(this.newProduit.prixGros) ||
      !prixValide(this.newProduit.prixDetail) ||
      !prixValide(this.newProduit.prixGerant)
    ) {
      this.errorMessage = 'Les prix doivent être des nombres positifs valides !';
      return;
    }

    const poidsRegex = /^\d+(\.\d+)?$/;
    if (
      typeof this.newProduit.poidsProduit !== 'number' ||
      this.newProduit.poidsProduit <= 0 ||
      !poidsRegex.test(this.newProduit.poidsProduit.toString())
    ) {
      this.errorMessage = 'Le poids doit être un nombre positif valide !';
      return;
    }

    this.errorMessage = '';

    this.produitService.addProduit(this.newProduit).subscribe(() => {
      this.loadProduits();
      this.toggleAddForm();
    });
  }

  editProduit(produit: Produit): void {
    this.selectedProduit = { ...produit };
    this.toggleEditForm();
  }

  toggleEditForm(): void {
    this.showEditForm = !this.showEditForm;
  }

  updateProduit(): void {
    if (this.selectedProduit) {
      this.produitService.updateProduit(this.selectedProduit).subscribe({
        next: (updatedProduit) => {

          const index = this.produits.findIndex(produit => produit.id === updatedProduit.id);
          if (index !== -1) {
            this.produits[index] = updatedProduit;
            this.filteredProduits = [...this.produits];
          }
          this.successMessage = 'Produit mis à jour avec succès !';
          this.toggleEditForm();
        }
      });
    }
  }

  toggleActions(produit: any): void {
    this.openedProduit = this.openedProduit === produit ? null : produit;
  }

  selectEmballageToEdit(produit: Produit): void {
    this.selectedProduit = { ...produit };
    this.showEditForm = true;
  }

  toggleDropdown(produit: Produit): void {
    if (!produit.checked) {
      this.showErrorPopup = true;
      this.errorMessage = "Veuillez d'abord sélectionner un produit.";
      return;
    }

    this.filteredProduits.forEach(p => {
      p['showDropdown'] = false;
    });

    produit['showDropdown'] = true;
  }

  @HostListener('document:click', ['$event'])
  onDocumentClick(event: MouseEvent): void {
    const target = event.target as HTMLElement;
    if (!target.closest('.actions-container')) {
      this.filteredProduits.forEach(produit => {
        produit['showDropdown'] = false;
      });
    }
  }

  deleteProduit(id: number): void {
    if (confirm("Êtes-vous sûr de vouloir supprimer ce produit ?")) {
      this.produitService.deleteProduit(id).subscribe({
        next: () => {
          this.loadProduits();
          this.successMessage = 'Produit supprimé avec succès !';
        }
      });
    }
  }
}
