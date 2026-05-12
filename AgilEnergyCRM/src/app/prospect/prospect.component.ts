import { Component, HostListener, OnInit } from '@angular/core';
import { ProspectService } from '../service/prospect.service';
import { Gouvernorat, Prospect, SourceProspection, SecteurActivite } from '../modeles/prospect.model';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { FilterPipe } from '../prospect/Filter';

@Component({
  selector: 'app-prospect',
  standalone: true,
  templateUrl: './prospect.component.html',
  styleUrl: './prospect.component.css',
  imports: [FormsModule, CommonModule, FilterPipe]
})

export class ProspectComponent implements OnInit {

  openedProspect: any;
  progressValue = 0;
  prospects: Prospect[] = [];
  filteredProspects: Prospect[] = [];
  searchTerm: string = '';
  errorMessage: string = '';
  successMessage: string = '';
  newProspect: Prospect = new Prospect();
  showAddForm: boolean = false;
  showEditForm: boolean = false;
  showErrorPopup: boolean = false;
  showDetailPopup: boolean = false;
  selectedProspects: Prospect[] = [];
  selectedProspect: Prospect | null = null;
  gouvernorats: Gouvernorat[] = [];
  sourceProspection: SourceProspection[] = [];
  secteurActivite: SecteurActivite[] = [];

  constructor(private prospectService: ProspectService, private router: Router) { }

  ngOnInit(): void {
    this.loadProspects();
    this.loadGouvernorats();
    this.loadSourceProspection();
    this.loadSecteurActivite();
  }

  convertirProspect(prospect: Prospect): void {
    this.prospectService.convertirEnClient(prospect).subscribe({
      next: client => {
        alert("Prospect converti en client avec succès !");
        this.loadProspects();
      },
      error: err => console.error("Erreur lors de la conversion", err)
    });
  }

  updateProgress() {
    let filled = 0;
    const total = 16;

    const p = this.newProspect;
    if (p.nom) filled++;
    if (p.titre) filled++;
    if (p.societe) filled++;
    if (p.chiffreA) filled++;
    if (p.effectif) filled++;
    if (p.email) filled++;
    if (p.autremail) filled++;
    if (p.telephone) filled++;
    if (p.fax) filled++;
    if (p.mobile) filled++;
    if (p.adresse) filled++;
    if (p.codePostal) filled++;
    if (p.ville) filled++;
    if (p.pays) filled++;
    if (p.statutprospect) filled++;
    if (p.sourceProspection?.id) filled++;
    if (p.secteurActivite?.id) filled++;
    if (p.gouvernorat?.id) filled++;

    this.progressValue = Math.floor((filled / total) * 100);
  }

  loadProspects(): void {
    this.prospectService.getAllProspects().subscribe({
      next: (prospects) => {

        this.prospects = prospects.filter(prospect => !prospect.isDeleted);
        this.filteredProspects = [...this.prospects];
      }
    });
  }

  loadGouvernorats(): void {
    this.prospectService.getGouvernorats().subscribe((data) => {
      this.gouvernorats = data;
    });
  }

  loadSourceProspection(): void {
    this.prospectService.getSourceProspection().subscribe((data) => {
      this.sourceProspection = data;
    });
  }

  loadSecteurActivite(): void {
    this.prospectService.getSecteurActivite().subscribe((data) => {
      this.secteurActivite = data;
    });
  }

  searchProspects(): void {
    if (this.searchTerm) {
      this.filteredProspects = this.prospects.filter(prospect =>
        prospect.nom.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
        prospect.email.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
        prospect.telephone.toString().includes(this.searchTerm) ||
        prospect.id?.toString().includes(this.searchTerm)
      );
    } else {
      this.filteredProspects = this.prospects;
    }
  }

  scrollTo(section: string) {
    const element = document.getElementById(section);
    if (element) {
      element.scrollIntoView({ behavior: 'smooth' });
    }
  }

  onProspectSelect(prospect: Prospect, event: any): void {
    const checked = event.target.checked;

    this.filteredProspects.forEach(p => {
      p['checked'] = false;
      p['showDropdown'] = false;
    });

    if (checked) {
      prospect['checked'] = true;
      this.selectedProspect = prospect;
    } else {
      this.selectedProspect = null;
    }
  }

  isSelected(prospect: Prospect): boolean {
    return this.selectedProspect?.id === prospect.id;
  }

  toggleAddForm(): void {
    this.showAddForm = !this.showAddForm;
    this.newProspect = new Prospect();
  }

  openPopup(prospect: Prospect): void {
    this.selectedProspect = prospect;
    this.showDetailPopup = true;
  }

  closePopup(): void {
    this.selectedProspect = null;
    this.showDetailPopup = false;
  }

  closeErrorPopup(): void {
    this.showErrorPopup = false;
    this.errorMessage = '';
  }

  addProspect(): void {
    if (!this.newProspect.nom || !this.newProspect.titre || !this.newProspect.societe || !this.newProspect.chiffreA ||
      !this.newProspect.effectif || !this.newProspect.statutprospect || !this.newProspect.email || !this.newProspect.telephone || !this.newProspect.adresse || !this.newProspect.codePostal ||
      !this.newProspect.ville || !this.newProspect.pays) {
      this.errorMessage = 'Tous les champs obligatoires doivent être remplis !';
      return;
    }

    const nameRegex = /^[a-zA-ZéèêàâîïôùçÉÈÊÀÂÎÏÔÙÇ\-\'\s]+$/;
    if (!nameRegex.test(this.newProspect.nom)) {
      this.errorMessage = 'Le nom ne doit contenir que des lettres.';
      return;
    }

    const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    if (!emailRegex.test(this.newProspect.email)) {
      this.errorMessage = 'L\'adresse email est invalide !';
      return;
    }

    const telStr = this.newProspect.telephone?.toString().trim();
    if (!/^\d{8}$/.test(telStr)) {
      this.errorMessage = 'Le numéro de téléphone doit contenir exactement 8 chiffres !';
      return;
    }

    const faxStr = this.newProspect.fax?.toString().trim();
    if (!/^\d{8}$/.test(faxStr)) {
      this.errorMessage = 'Le numéro de Fax doit contenir exactement 8 chiffres !';
      return;
    }

    const mobStr = this.newProspect.mobile?.toString().trim();
    if (!/^\d{8}$/.test(mobStr)) {
      this.errorMessage = 'Le numéro mobile doit contenir exactement 8 chiffres !';
      return;
    }

    if (this.newProspect.codePostal <= 0) {
      this.errorMessage = 'Le code postal doit être valide !';
      return;
    }

    this.errorMessage = '';

    this.prospectService.addProspect(this.newProspect).subscribe(() => {
      this.loadProspects();
      this.toggleAddForm();
    });
  }

  editProspect(prospect: Prospect): void {

    this.selectedProspect = { ...prospect };

    this.selectedProspect.gouvernorat = this.gouvernorats.find(g => g.id === prospect.gouvernorat?.id)!;
    this.selectedProspect.sourceProspection = this.sourceProspection.find(s => s.id === prospect.sourceProspection?.id)!;
    this.selectedProspect.secteurActivite = this.secteurActivite.find(s => s.id === prospect.secteurActivite?.id)!;

    this.toggleEditForm();
  }

  toggleEditForm(): void {
    this.showEditForm = !this.showEditForm;
  }

  toggleDropdown(prospect: Prospect): void {
    if (!prospect.checked) {
      this.showErrorPopup = true;
      this.errorMessage = "Veuillez d'abord sélectionner un prospect.";
      return;
    }

    this.filteredProspects.forEach(p => {
      p['showDropdown'] = false;
    });

    prospect['showDropdown'] = true;
  }

  @HostListener('document:click', ['$event'])
  onDocumentClick(event: MouseEvent): void {
    const target = event.target as HTMLElement;
    if (!target.closest('.actions-container')) {
      this.filteredProspects.forEach(prospect => {
        prospect['showDropdown'] = false;
      });
    }
  }

  updateProspect(): void {
    if (this.selectedProspect) {
      this.prospectService.updateProspect(this.selectedProspect).subscribe({
        next: (updatedProspect) => {
          const index = this.prospects.findIndex(prospect => prospect.id === updatedProspect.id);
          if (index !== -1) {
            this.prospects[index] = updatedProspect;
            this.filteredProspects = [...this.prospects];
          }
          this.successMessage = 'Prospect mis à jour avec succès !';
          this.toggleEditForm();
        }
      });
    }
  }

  selectProspectToEdit(prospect: Prospect): void {
    this.selectedProspect = { ...prospect };
    this.showEditForm = true;
  }

  deleteProspect(id: number): void {
    if (confirm("Êtes-vous sûr de vouloir supprimer ce prospect ?")) {
      this.prospectService.deleteProspect(id).subscribe({
        next: () => {
          this.prospects = this.prospects.filter(prospect => prospect.id !== id);
          this.filteredProspects = [...this.prospects];
          this.successMessage = 'Prospect supprimé avec succès !';
        }
      });
    }
  }
}
