import { Component, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ParametrageService } from '../service/parametrage.service';
import { Tva, SourceProspection, SecteurActivite } from '../modeles/parametrage.model';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';

@Component({
  selector: 'app-parametrage',
  standalone: true,
  templateUrl: './parametrage.component.html',
  styleUrl: './parametrage.component.css',
  imports: [CommonModule, FormsModule]
})
export class ParametrageComponent implements OnInit, OnChanges {
  selectedParam: string = 'TVA';
  tva?: Tva;
  newTvaValue!: number;
  sources: SourceProspection[] = [];
  newSourceName: string = '';
  secteurs: SecteurActivite[] = [];
  newSecteurName: string = '';
  tvaList: Tva[] = [];
  tvaValue: number = 0;
  errorMessage: string = '';
  successMessage: string = '';

  constructor(private paramService: ParametrageService, private router: Router) { }

  ngOnInit(): void {
    this.loadCurrentParam();
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['selectedParam']) {
      this.loadCurrentParam();
    }
  }

  scrollTo(section: string): void {
    const element = document.getElementById(section);
    if (element) {
      element.scrollIntoView({ behavior: 'smooth' });
    }
  }

  loadCurrentParam(): void {
    if (this.selectedParam === 'TVA') {
      this.paramService.getAllTVA().subscribe(tva => {
        this.tvaList = tva;
        this.newTvaValue = tva.length > 0 ? tva[0].valeur : 0;
      });
    } else if (this.selectedParam === 'SOURCE') {
      this.paramService.getSources().subscribe(sources => this.sources = sources);
    } else if (this.selectedParam === 'SECTEUR') {
      this.paramService.getSecteurs().subscribe(secteurs => this.secteurs = secteurs);
    }
  }

  updateTva(): void {
    if (this.newTvaValue <= 0 || isNaN(this.newTvaValue)) {
      this.errorMessage = 'La TVA doit être un nombre positif.';
      this.clearMessagesAfterDelay();
      return;
    }
    this.errorMessage = '';
    const updatedTva = this.newTvaValue;
  
    this.paramService.updateTva(updatedTva).subscribe({
      next: (response) => {
        this.successMessage = 'TVA mise à jour avec succès !';
        this.clearMessagesAfterDelay();
        this.tvaValue = response.valeur;
        this.newTvaValue = response.valeur;
      },
      error: (err) => {
        this.errorMessage = 'Erreur lors de la mise à jour de la TVA.';
        this.clearMessagesAfterDelay();
      }
    });
  }
  
  notifyTvaUpdate(updatedTvaValue: number): void {}  

  addSource(): void {
    const regex = /^[A-Za-zÀ-ÿ\s]+$/;
    if (!regex.test(this.newSourceName.trim())) {
      this.errorMessage = "La source de prospection doit uniquement contenir des lettres.";
      this.clearMessagesAfterDelay();
      return;
    }
    if (this.newSourceName?.trim()) {
      const src: SourceProspection = { nom: this.newSourceName.trim() };
      this.paramService.addSource(src).subscribe(() => {
        this.newSourceName = '';
        this.loadCurrentParam();
        this.successMessage = "Source de prospection ajoutée avec succès !";
        this.clearMessagesAfterDelay();
      });
    } else {
      this.errorMessage = "La source de prospection ne peut pas être vide !";
      this.clearMessagesAfterDelay();
    }
  }

  deleteSource(id: number): void {
    if (confirm("Êtes-vous sûr de vouloir supprimer cette Source de Prospection ?")) {
      this.paramService.deleteSource(id).subscribe(() => {
        this.loadCurrentParam();
        this.successMessage = "Source de prospection supprimée avec succès !";
        this.clearMessagesAfterDelay();
      });
    }
  }

  addSecteur(): void {
    const regex = /^[A-Za-zÀ-ÿ\s]+$/;
    if (!regex.test(this.newSecteurName.trim())) {
      this.errorMessage = "Le secteur d'activité doit uniquement contenir des lettres.";
      this.clearMessagesAfterDelay();
      return;
    }
    if (this.newSecteurName.trim() !== '') {
      const secteur = { nom: this.newSecteurName };
      this.paramService.addSecteur(secteur).subscribe(() => {
        this.newSecteurName = '';
        this.loadCurrentParam();
        this.successMessage = "Secteur d'activité ajouté avec succès !";
        this.clearMessagesAfterDelay();
      });
    } else {
      this.errorMessage = "Le secteur d'activité ne peut pas être vide !";
      this.clearMessagesAfterDelay();
    }
  }

  deleteSecteur(id: number): void {
    this.paramService.deleteSecteur(id).subscribe(() => {
      this.loadCurrentParam();
      this.successMessage = "Secteur d'activité supprimé avec succès !";
      this.clearMessagesAfterDelay();
    });
  }

  private clearMessagesAfterDelay(): void {
    setTimeout(() => {
      this.errorMessage = '';
      this.successMessage = '';
    }, 5000);
  }

}
