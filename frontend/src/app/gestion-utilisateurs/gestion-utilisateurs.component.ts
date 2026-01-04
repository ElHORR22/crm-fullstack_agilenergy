import { Component, HostListener, OnInit } from '@angular/core';
import { Role, Utilisateur } from '../modeles/utilisateur.model';
import { GestionUtilisateurService } from '../service/gestion_utilisateur.service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';

@Component({
  selector: 'app-utilisateurs-management',
  standalone: true,
  templateUrl: './gestion-utilisateurs.component.html',
  styleUrl: './gestion-utilisateurs.component.css',
  imports: [FormsModule, CommonModule]
})
export class GestionUtilisateurComponent implements OnInit {

  roles: Role[] = [];
  utilisateurs: Utilisateur[] = [];
  filteredUtilisateurs: Utilisateur[] = [];
  selectedUtilisateur: Utilisateur | null = null;
  newUtilisateur: Utilisateur = {} as Utilisateur;
  openedUtilisateur: any;
  searchTerm: string = '';
  showAddForm: boolean = false;
  showEditForm: boolean = false;
  showDetailPopup: boolean = false;
  showErrorPopup: boolean = false;
  errorMessage: string = '';
  successMessage: string = '';
  progressValue: number = 0;

  constructor(private gestionUtilisateurService: GestionUtilisateurService, private router: Router) { }
  
  ngOnInit(): void {
    this.loadUtilisateurs();
    this.loadRoles();
  }  

  loadUtilisateurs(): void {
    this.gestionUtilisateurService.getAllUtilisateurs().subscribe({
      next: (data) => {
        this.utilisateurs = data;
        this.filteredUtilisateurs = [...this.utilisateurs];
      },
      error: () => this.errorMessage = 'Erreur lors du chargement des utilisateurs.'
    });
  }

  loadRoles(): void {
    this.gestionUtilisateurService.getAllRoles().subscribe(
      (data: Role[]) => {
        this.roles = data;
        console.log('Rôles chargés :', this.roles);
      },
      (err) => {
        console.error("Erreur lors du chargement des rôles :", err);
      }
    );
  }    

  updateProgress(): void {
    let filled = 0;
    const total = 5;

    const u = this.newUtilisateur;
    if (u.email) filled++;
    if (u.mdp) filled++;
    if (u.nom) filled++;
    if (u.prenom) filled++;
    if (u.role) filled++;

    this.progressValue = Math.floor((filled / total) * 100);
  }

  searchUtilisateurs(): void {
    if (this.searchTerm) {
      this.filteredUtilisateurs = this.utilisateurs.filter(user =>
        user.nom?.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
        user.email?.toLowerCase().includes(this.searchTerm.toLowerCase())
      );
    } else {
      this.filteredUtilisateurs = this.utilisateurs;
    }
  }

  toggleAddForm(): void {
    this.showAddForm = !this.showAddForm;
    this.newUtilisateur = {} as Utilisateur;
  }

  openPopup(utilisateur: Utilisateur): void {
    this.selectedUtilisateur = utilisateur;
    this.showDetailPopup = true;
  }

  closePopup(): void {
    this.selectedUtilisateur = null;
    this.showDetailPopup = false;
  }

  closeErrorPopup(): void {
    this.showErrorPopup = false;
    this.errorMessage = '';
  }

  addUtilisateur(): void {
    if (!this.newUtilisateur.email || !this.newUtilisateur.mdp || !this.newUtilisateur.nom || !this.newUtilisateur.prenom || !this.newUtilisateur.role) {
      this.errorMessage = 'Veuillez remplir tous les champs obligatoires.';
      return;
    }
  
    console.log('Utilisateur envoyé:', this.newUtilisateur);
  
    this.gestionUtilisateurService.addUtilisateur(this.newUtilisateur).subscribe({
      next: () => {
        this.loadUtilisateurs();
        this.successMessage = "Utilisateur ajouté avec succès";
        this.toggleAddForm();
        this.errorMessage = "";
      },
      error: (err) => {
        console.error(err);
        this.errorMessage = "Erreur lors de l'ajout";
      }
    });
  }   

  editUtilisateur(utilisateur: Utilisateur): void {
    this.selectedUtilisateur = { ...utilisateur };
    this.toggleEditForm();
  }

  toggleEditForm(): void {
    this.showEditForm = !this.showEditForm;
  }

  updateUtilisateur(): void {
    if (this.selectedUtilisateur) {
      this.gestionUtilisateurService.updateUtilisateur(this.selectedUtilisateur).subscribe({
        next: (updatedUtilisateur) => {

          const index = this.utilisateurs.findIndex(utilisateur => utilisateur.id === updatedUtilisateur.id);
          if (index !== -1) {
            this.utilisateurs[index] = updatedUtilisateur;
            this.filteredUtilisateurs = [...this.utilisateurs];
          }
          this.successMessage = 'Produit mis à jour avec succès !';
          this.toggleEditForm();
        }
      });
    }
  }

  deleteUtilisateur(id: number): void {
    if (confirm("Êtes-vous sûr de vouloir supprimer cet utilisateur ?")) {
      this.gestionUtilisateurService.deleteUtilisateur(id).subscribe({
        next: () => {
          this.loadUtilisateurs();
          this.successMessage = "Utilisateur supprimé";
        }
      });
    }
  }

  toggleActions(utilisateur: any): void {
    this.openedUtilisateur = this.openedUtilisateur === utilisateur ? null : utilisateur;
  }

  toggleDropdown(utilisateur: any): void {
    if (!utilisateur.checked) {
      this.showErrorPopup = true;
      this.errorMessage = "Veuillez d'abord sélectionner un utilisateur.";
      return;
    }

    this.filteredUtilisateurs.forEach(u => u['showDropdown'] = false);
    utilisateur['showDropdown'] = true;
  }

  onUtilisateurSelect(utilisateur: Utilisateur, event: any): void {
    const checked = event.target.checked;
    this.filteredUtilisateurs.forEach(u => {
      u['checked'] = false;
      u['showDropdown'] = false;
    });

    if (checked) {
      utilisateur['checked'] = true;
      this.selectedUtilisateur = utilisateur;
    } else {
      this.selectedUtilisateur = null;
    }
  }

  @HostListener('document:click', ['$event'])
  onDocumentClick(event: MouseEvent): void {
    const target = event.target as HTMLElement;
    if (!target.closest('.actions-container')) {
      this.filteredUtilisateurs.forEach(u => {
        u['showDropdown'] = false;
      });
    }
  }
}
