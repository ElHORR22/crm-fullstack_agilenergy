import { Component, HostListener, OnInit } from '@angular/core';
import { EmballageService } from '../service/emballage.service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { FiltrePipe } from './FiltreEmballage';
import { Emballage } from '../modeles/emballage.model';
import { Router } from '@angular/router';


@Component({
  selector: 'app-emballage',
  standalone: true,
  templateUrl: './emballage.component.html',
  styleUrl: './emballage.component.css',
  imports: [FormsModule, CommonModule, FiltrePipe]
})

export class EmballageComponent implements OnInit {

  openedEmballage: any;
  progressValue = 0;
  emballages: Emballage[] = [];
  filteredEmballages: Emballage[] = [];
  searchTerm: string = '';
  errorMessage: string = '';
  successMessage: string = '';
  selectedEmballages: Emballage[] = [];
  selectedEmballage: Emballage | null = null;
  newEmballage: Emballage = new Emballage();
  showAddForm: boolean = false;
  showEditForm: boolean = false;
  showErrorPopup: boolean = false;
  showDetailPopup: boolean = false;

  constructor(private emballageService: EmballageService, private router: Router) { }

  ngOnInit(): void {
    this.loadEmballages();
  }

  loadEmballages(): void {
    this.emballageService.getAllEmballages().subscribe({
      next: (emballages) => {

        this.emballages = emballages.filter(emballage => !emballage.isDeleted);
        this.filteredEmballages = [...this.emballages];
      }
    });
  }

  updateProgress() {
    let filled = 0;
    const total = 4;

    const p = this.newEmballage;
    if (p.codeEmballage) filled++;
    if (p.libelle) filled++;
    if (p.poidsEmballage) filled++;
    if (p.typemballage) filled++;

    this.progressValue = Math.floor((filled / total) * 100);
  }

  searchEmballages(): void {
    if (this.searchTerm) {
      this.filteredEmballages = this.emballages.filter(emballage =>
        emballage.codeEmballage.toString().includes(this.searchTerm) ||
        emballage.id?.toString().includes(this.searchTerm)
      );
    } else {
      this.filteredEmballages = this.emballages;
    }
  }

  scrollTo(section: string): void {
    const element = document.getElementById(section);
    if (element) {
      element.scrollIntoView({ behavior: 'smooth' });
    }
  }

  onEmballageSelect(emballage: Emballage, event: any): void {
    const checked = event.target.checked;
    this.filteredEmballages.forEach(e => {
      e['checked'] = false;
      e['showDropdown'] = false;
    });

    if (checked) {
      emballage['checked'] = true;
      this.selectedEmballage = emballage;
    } else {
      this.selectedEmballage = null;
    }
  }

  isSelected(emballage: Emballage): boolean {
    return this.selectedEmballages.some(e => e.id === emballage.id);
  }

  toggleAddForm(): void {
    this.showAddForm = !this.showAddForm;
    this.newEmballage = new Emballage();
  }

  openPopup(emballage: Emballage): void {
    this.selectedEmballage = emballage;
    this.showDetailPopup = true;
  }

  closePopup(): void {
    this.selectedEmballage = null;
    this.showDetailPopup = false;
  }

  closeErrorPopup(): void {
    this.showErrorPopup = false;
    this.errorMessage = '';
  }

  addEmballage(): void {
    if (!this.newEmballage.codeEmballage || !this.newEmballage.libelle || !this.newEmballage.poidsEmballage) {
      this.errorMessage = 'Tous les champs obligatoires doivent être remplis !';
      return;
    }

    const codeRegex = /^\d+$/;
    if (!codeRegex.test(this.newEmballage.codeEmballage.toString())) {
      this.errorMessage = 'Le code doit contenir uniquement des chiffres !';
      return;
    }

    const poidsRegex = /^\d+(\.\d+)?$/;
    if (
      typeof this.newEmballage.poidsEmballage !== 'number' ||
      this.newEmballage.poidsEmballage <= 0 ||
      !poidsRegex.test(this.newEmballage.poidsEmballage.toString())
    ) {
      this.errorMessage = 'Le poids doit être un nombre positif valide !';
      return;
    }

    this.errorMessage = '';

    this.emballageService.addEmballage(this.newEmballage).subscribe(() => {
      this.loadEmballages();
      this.toggleAddForm();
    });
  }

  editEmballage(emballage: Emballage): void {
    this.selectedEmballage = { ...emballage };
    this.toggleEditForm();
  }

  toggleEditForm(): void {
    this.showEditForm = !this.showEditForm;
  }

  updateEmballage(): void {
    if (this.selectedEmballage) {
      this.emballageService.updateEmballage(this.selectedEmballage).subscribe({
        next: (updatedEmballage) => {

          const index = this.emballages.findIndex(emballage => emballage.id === updatedEmballage.id);
          if (index !== -1) {
            this.emballages[index] = updatedEmballage;
            this.filteredEmballages = [...this.emballages];
          }
          this.successMessage = 'Emballage mis à jour avec succès !';
          this.toggleEditForm();
        }
      });
    }
  }

  toggleActions(emballage: any): void {
    this.openedEmballage = this.openedEmballage === emballage ? null : emballage;
  }

  selectEmballageToEdit(emballage: Emballage): void {
    this.selectedEmballage = { ...emballage };
    this.showEditForm = true;
  }

  toggleDropdown(emballage: Emballage): void {
    if (!emballage.checked) {
      this.showErrorPopup = true;
      this.errorMessage = "Veuillez d'abord sélectionner un emballage.";
      return;
    }

    this.filteredEmballages.forEach(p => {
      p['showDropdown'] = false;
    });

    emballage['showDropdown'] = true;
  }

  @HostListener('document:click', ['$event'])
  onDocumentClick(event: MouseEvent): void {
    const target = event.target as HTMLElement;
    if (!target.closest('.actions-container')) {
      this.filteredEmballages.forEach(emballage => {
        emballage['showDropdown'] = false;
      });
    }
  }

  deleteEmballage(id: number): void {
    if (confirm("Êtes-vous sûr de vouloir supprimer cet emballage ?")) {
      this.emballageService.deleteEmballage(id).subscribe({
        next: () => {
          this.loadEmballages();
          this.successMessage = 'Emballage supprimé avec succès !';
        }
      });
    }
  }
}