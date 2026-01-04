import { Component, OnInit, OnDestroy } from '@angular/core';
import { AbstractControl, FormArray, FormBuilder, FormGroup, ValidationErrors, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';

import { DevisService } from '../service/devis.service';
import { ProspectService } from '../service/prospect.service';
import { ClientService } from '../service/client.service';
import { ProduitService } from '../service/produit.service';
import { EmballageService } from '../service/emballage.service';
import { ParametrageService } from '../service/parametrage.service';

import { Devis } from '../modeles/devis.model';
import { Prospect } from '../modeles/prospect.model';
import { Client } from '../modeles/client.model';
import { Produit } from '../modeles/produit.model';
import { Emballage } from '../modeles/emballage.model';
import { Tva } from '../modeles/parametrage.model';

import { FilterPipe } from './Filter';
import { forkJoin, Subject, takeUntil } from 'rxjs';

@Component({
  selector: 'app-devis',
  standalone: true,
  templateUrl: './devis.component.html',
  styleUrl: './devis.component.css',
  imports: [CommonModule, ReactiveFormsModule, FormsModule, FilterPipe]
})
export class DevisComponent implements OnInit, OnDestroy {

  progressValue = 0;
  devisForm!: FormGroup;

  isEditMode = false;
  isClientSelected = true;
  isProspect = false;
  currentDevisId?: number;

  prospects: Prospect[] = [];
  filteredProspects: Prospect[] = [];
  clients: Client[] = [];
  filteredClients: Client[] = [];

  produits: Produit[] = [];
  emballages: Emballage[] = [];

  totalTTC = 0;
  totalPoidsKg = 0;

  searchTerm = '';
  filteredDevis: Devis[] = [];
  devisList: Devis[] = [];

  showAddForm = false;
  showEditForm = false;

  selectedDevis: Devis | null = null;

  successMessage = '';

  tvaValue = 0;
  tvaList: Tva[] = [];

  errorMessage = '';
  showErrorPopup = false;
  showDetailPopup = false;

  private destroy$ = new Subject<void>();

  // ✅ handler stable pour add/removeEventListener
  private onDocClick = (e: MouseEvent) => this.closeAllDropdownsOnOutsideClick(e);

  constructor(
    private fb: FormBuilder,
    private devisService: DevisService,
    private prospectService: ProspectService,
    private clientService: ClientService,
    private produitService: ProduitService,
    private emballageService: EmballageService,
    private paramService: ParametrageService
  ) { }

  ngOnInit(): void {
    this.devisForm = this.fb.group({
      sujet: ['', Validators.required],
      client: [null],
      prospect: [null],
      echeance: ['', [Validators.required, this.dateMinValidator]],
      delaiLivraison: ['', Validators.required],
      modeLivraison: ['', Validators.required],
      modePaiement: ['', Validators.required],
      lignes: this.fb.array([]),

      // gardé si ton UI l’affiche, sinon tu peux l’enlever
      TVA: [this.tvaValue]
    });

    // ✅ progression propre
    this.devisForm.valueChanges
      .pipe(takeUntil(this.destroy$))
      .subscribe(() => this.updateProgress());

    forkJoin([
      this.clientService.getAllClients(),
      this.prospectService.getAllProspects(),
      this.produitService.getAllProduits(),
      this.emballageService.getAllEmballages(),
      this.devisService.getAllDevis(),
      this.devisService.getTVA(),
      this.paramService.getAllTVA()
    ])
    .pipe(takeUntil(this.destroy$))
    .subscribe(([clients, prospects, produits, emballages, devis, tvaUnique, tvaList]) => {
      this.filteredClients = this.clients = clients;
      this.filteredProspects = this.prospects = prospects.filter(p => !p.isDeleted);
      this.produits = produits.filter(p => !p.isDeleted);
      this.emballages = emballages.filter(e => !e.isDeleted);

      this.filteredDevis = this.devisList = devis;

      this.tvaList = tvaList;
      this.tvaValue = tvaList?.[0]?.valeur ?? tvaUnique;
      this.devisForm.get('TVA')?.setValue(this.tvaValue);

      document.addEventListener('click', this.onDocClick);
      this.updateProgress();
    });
  }

  ngOnDestroy() {
    document.removeEventListener('click', this.onDocClick);
    this.destroy$.next();
    this.destroy$.complete();
  }

  // ----------------- UI helpers -----------------

  closeAllDropdownsOnOutsideClick(event: any) {
    const clickedInside = event.target.closest('.actions-container');
    if (!clickedInside) {
      this.filteredDevis.forEach(d => d['showDropdown'] = false);
    }
  }

  loadDevis(): void {
    this.devisService.getAllDevis().subscribe({
      next: (devis) => {
        this.devisList = devis;
        this.filteredDevis = devis;
      },
      error: () => {
        this.errorMessage = "Erreur lors du chargement des devis.";
        this.showErrorPopup = true;
      }
    });
  }

  get lignes(): FormArray {
    return this.devisForm.get('lignes') as FormArray;
  }

  updateProgress() {
    const v = this.devisForm?.value;
    if (!v) return;

    let filled = 0;
    const total = 6;

    if (v.sujet) filled++;
    if (v.echeance) filled++;
    if (v.delaiLivraison) filled++;
    if (v.modeLivraison) filled++;
    if (v.modePaiement) filled++;
    if (this.lignes.length > 0) filled++;

    this.progressValue = Math.floor((filled / total) * 100);
  }

  scrollTo(section: string): void {
    const element = document.getElementById(section);
    if (element) element.scrollIntoView({ behavior: 'smooth' });
  }

  searchDevis(): void {
    if (!this.searchTerm) {
      this.filteredDevis = this.devisList;
      return;
    }
    this.filteredDevis = this.devisList.filter(devis =>
      devis.id?.toString().includes(this.searchTerm) ||
      devis.sujetDevis.toLowerCase().includes(this.searchTerm.toLowerCase())
    );
  }

  toggleAddForm(): void {
    if (this.isEditMode) this.showEditForm = !this.showEditForm;
    else this.showAddForm = !this.showAddForm;
  }

  openPopup(devis: Devis): void {
    this.selectedDevis = devis;
    this.showDetailPopup = true;
  }

  closePopup(): void {
    this.selectedDevis = null;
    this.showDetailPopup = false;
  }

  closeErrorPopup(): void {
    this.showErrorPopup = false;
    this.errorMessage = '';
  }

  toggleDropdown(devis: Devis): void {
    if (!devis.checked) {
      this.showErrorPopup = true;
      this.errorMessage = "Veuillez d'abord sélectionner un devis.";
      return;
    }

    const currentState = devis['showDropdown'];
    this.filteredDevis.forEach(p => p['showDropdown'] = false);
    devis['showDropdown'] = !currentState;
  }

  onDevisSelect(devis: Devis, event: any): void {
    const checked = event.target.checked;
    this.filteredDevis.forEach(e => {
      e['checked'] = false;
      e['showDropdown'] = false;
    });

    if (checked) {
      devis['checked'] = true;
      this.selectedDevis = devis;
    } else {
      this.selectedDevis = null;
    }
  }

  // ----------------- lignes -----------------

  addLigne(): void {
    const ligne = this.fb.group({
      codeProduit: ['', Validators.required],
      libelleProduit: [''],
      codeEmballage: ['', Validators.required],
      quantite: [1, [Validators.required, Validators.min(1)]],
      prixUnitaireHT: [0, Validators.required],

      // UI calcul
      ecoZit: [0],
      prixTTC: [0],
      poidsLigneKg: [0],
      poidsProduit: [0],
      poidsEmballage: [0],

      // TVA juste pour affichage/calcul (backend utilise TVA id=1)
      TVA: [this.tvaValue]
    });

    this.lignes.push(ligne);
    this.updateProgress();
  }

  removeLigne(index: number): void {
    this.lignes.removeAt(index);
    this.calculateTotal();
    this.updateProgress();
  }

  dateMinValidator(control: AbstractControl): ValidationErrors | null {
    const inputDate = new Date(control.value);
    const today = new Date();
    today.setHours(0, 0, 0, 0);
    return inputDate >= today ? null : { dateMin: true };
  }

  calculateTotal(): void {
    this.totalTTC = 0;
    this.totalPoidsKg = 0;

    this.lignes.controls.forEach((ligne: any) => {
      const quantite = ligne.get('quantite')?.value ?? 0;
      const prixUnitaireHT = ligne.get('prixUnitaireHT')?.value ?? 0;
      const tvaValue = ligne.get('TVA')?.value ?? this.tvaValue;

      const poidsProduit = ligne.get('poidsProduit')?.value ?? 0;
      const poidsEmballage = ligne.get('poidsEmballage')?.value ?? 0;

      let poidsLigne = ligne.get('poidsLigneKg')?.value ?? 0;
      let prixTTC = ligne.get('prixTTC')?.value ?? 0;

      if (poidsProduit > 0 || poidsEmballage > 0) {
        const ecoZit = parseFloat(Math.max(0, (poidsProduit - poidsEmballage) * 0.06 * quantite).toFixed(3));
        poidsLigne = parseFloat(Math.max(0, (poidsProduit + poidsEmballage) * quantite).toFixed(3));
        prixTTC = parseFloat(Math.max(0, (prixUnitaireHT * quantite * (1 + tvaValue / 100)) + ecoZit).toFixed(3));

        ligne.get('ecoZit')?.setValue(ecoZit, { emitEvent: false });
        ligne.get('poidsLigneKg')?.setValue(poidsLigne, { emitEvent: false });
        ligne.get('prixTTC')?.setValue(prixTTC, { emitEvent: false });
      }

      this.totalTTC += prixTTC;
      this.totalPoidsKg += poidsLigne;
    });
  }

  onLigneChange(index: number): void {
    const ligne = this.lignes.at(index);
    const { codeProduit, codeEmballage } = ligne.value;

    const produit = this.produits.find(p => p.codeProduit === codeProduit);
    const emballage = this.emballages.find(e => e.codeEmballage === codeEmballage);

    ligne.patchValue({
      poidsProduit: produit?.poidsProduit ?? 0,
      poidsEmballage: emballage?.poidsEmballage ?? 0
    }, { emitEvent: false });

    this.calculateTotal();
  }

  onProduitChange(i: number): void {
    const ligne = this.lignes.at(i);
    const codeProduit = ligne.get('codeProduit')?.value;
    const selectedEmballage = ligne.get('codeEmballage')?.value;

    const produit = this.produits.find(p => p.codeProduit === codeProduit);
    const emballage = this.getEmballage(selectedEmballage);

    if (!produit || !emballage) return;

    let typePrix = 'Prix de gros';
    if (!this.isProspect) {
      const client = this.devisForm.get('client')?.value;
      if (client?.typeprixAchat) typePrix = client.typeprixAchat;
    }

    let prixHT = 0;
    switch (typePrix) {
      case 'Prix de gros': prixHT = produit.prixGros; break;
      case 'Prix gérant': prixHT = produit.prixGerant; break;
      case 'Prix de détail': prixHT = produit.prixDetail; break;
    }

    ligne.patchValue({
      libelleProduit: produit.libelleProduit,
      prixUnitaireHT: prixHT,
      poidsProduit: produit.poidsProduit ?? 0,
      poidsEmballage: emballage.poidsEmballage ?? 0
    }, { emitEvent: false });

    this.calculateTotal();
  }

  onEmballageChange(i: number): void {
    const ligne = this.lignes.at(i);
    const selectedEmballage = ligne.get('codeEmballage')?.value;
    const emballage = this.getEmballage(selectedEmballage);

    ligne.get('poidsEmballage')?.setValue(emballage?.poidsEmballage ?? 0, { emitEvent: false });
    this.calculateTotal();
  }

  private getEmballage(selectedEmballage: any): Emballage | undefined {
    return typeof selectedEmballage === 'object'
      ? selectedEmballage
      : this.emballages.find(e => e.codeEmballage === selectedEmballage);
  }

  toggleClientProspect(type: 'client' | 'prospect'): void {
    this.isClientSelected = type === 'client';
    this.isProspect = type === 'prospect';

    if (!this.isEditMode) {
      this.devisForm.patchValue({ client: null, prospect: null }, { emitEvent: false });
    }

    this.lignes.controls.forEach((_, i) => this.onProduitChange(i));
  }

  // ----------------- save / edit -----------------

  saveDevis(): void {
    if (this.devisForm.invalid || this.lignes.length === 0) {
      this.errorMessage = this.devisForm.invalid
        ? 'Tous les champs obligatoires doivent être remplis.'
        : 'Le devis doit contenir au moins une ligne.';
      this.showErrorPopup = true;
      this.devisForm.markAllAsTouched();
      return;
    }

    const form = this.devisForm.value;

    const clientId = this.isClientSelected ? (form.client?.idC ?? form.client?.id ?? null) : null;
    const prospectId = this.isProspect ? (form.prospect?.idP ?? form.prospect?.id ?? null) : null;

    if (clientId && prospectId) {
      this.errorMessage = "Choisir soit un client soit un prospect (pas les deux).";
      this.showErrorPopup = true;
      return;
    }
    if (!clientId && !prospectId) {
      this.errorMessage = "Veuillez sélectionner un client ou un prospect.";
      this.showErrorPopup = true;
      return;
    }

    const devisToSend: any = {
      sujetDevis: form.sujet || '',
      echeance: form.echeance,
      delaiLivraison: form.delaiLivraison,
      modeLivraison: form.modeLivraison,
      modePaiement: form.modePaiement,
      clientId,
      prospectId,
      ligneDevis: this.lignes.value.map((l: any) => ({
        codeProduit: l.codeProduit,
        libelleProduit: l.libelleProduit,
        codeEmballage: typeof l.codeEmballage === 'object' ? l.codeEmballage.codeEmballage : l.codeEmballage,
        quantite: l.quantite,
        prixUnitaireHT: l.prixUnitaireHT,
        ecoZit: l.ecoZit ?? 0,
        poidsLigneKg: l.poidsLigneKg ?? 0
      }))
    };

    const op = this.isEditMode && this.currentDevisId
      ? this.devisService.updateDevis(this.currentDevisId, devisToSend)
      : this.devisService.addDevis(devisToSend);

    op.subscribe({
      next: () => {
        this.successMessage = this.isEditMode ? 'Devis mis à jour !' : 'Devis créé !';
        this.resetFormUI();
      },
      error: (err) => this.handleApiError(err)
    });
  }

  private handleApiError(err: any) {
    const fieldErrors = err?.error?.fieldErrors;
    if (fieldErrors) {
      this.errorMessage = Object.values(fieldErrors).join(' | ');
    } else {
      this.errorMessage = err?.error?.message ?? 'Erreur lors de l’enregistrement.';
    }
    this.showErrorPopup = true;
  }

  private resetFormUI(): void {
    this.isEditMode = false;
    this.showAddForm = false;
    this.showEditForm = false;

    this.devisForm.reset();
    this.lignes.clear();

    this.totalTTC = 0;
    this.totalPoidsKg = 0;
    this.progressValue = 0;

    this.devisService.getAllDevis().subscribe(devis => this.devisList = this.filteredDevis = devis);
    setTimeout(() => this.successMessage = '', 3000);
  }

  editDevis(devis: Devis): void {
    this.selectedDevis = { ...devis };
    this.currentDevisId = devis.id;
    this.isEditMode = true;
    this.showEditForm = true;

    this.isClientSelected = !!devis.client;
    this.isProspect = !!devis.prospect;

    this.devisForm.patchValue({
      sujet: devis.sujetDevis,
      // ⚠️ selon tes modèles: idC/idP (pas id)
      client: devis.client ? (this.clients.find(c => (c as any).idC === (devis.client as any).idC) ?? null) : null,
      prospect: devis.prospect ? (this.prospects.find(p => (p as any).idP === (devis.prospect as any).idP) ?? null) : null,
      echeance: devis.echeance,
      delaiLivraison: devis.delaiLivraison,
      modeLivraison: devis.modeLivraison,
      modePaiement: devis.modePaiement
    }, { emitEvent: false });

    this.lignes.clear();

    devis.ligneDevis?.forEach(l => this.lignes.push(this.fb.group({
      codeProduit: [l.codeProduit, Validators.required],
      libelleProduit: [l.libelleProduit],
      codeEmballage: [this.emballages.find(e => e.codeEmballage?.toString() === l.codeEmballage?.toString()) ?? null, Validators.required],
      quantite: [l.quantite, [Validators.required, Validators.min(1)]],
      prixUnitaireHT: [l.prixUnitaireHT ?? 0, Validators.required],

      // UI calcul
      TVA: [this.tvaValue],
      ecoZit: [l.ecoZit ?? 0],
      prixTTC: [l.prixTTC ?? 0],
      poidsLigneKg: [l.poidsLigneKg ?? 0],
      poidsProduit: [0],
      poidsEmballage: [0]
    })));

    this.totalTTC = devis.totalTTC ?? 0;
    this.totalPoidsKg = devis.totalPoidsKg ?? 0;

    this.calculateTotal();
    this.updateProgress();
  }

  deleteDevis(id: number): void {
    if (!confirm('Confirmer la suppression du devis ?')) return;

    this.devisService.deleteDevis(id).subscribe({
      next: () => {
        this.devisService.getAllDevis().subscribe(devis => {
          this.devisList = this.filteredDevis = devis;
        });
        this.successMessage = 'Devis supprimé avec succès !';
        setTimeout(() => this.successMessage = '', 3000);
      },
      error: () => {
        this.errorMessage = "Erreur lors de la suppression.";
        this.showErrorPopup = true;
      }
    });
  }

  resetForm(): void {
    this.devisForm.reset();
    this.lignes.clear();
    this.isEditMode = false;
    this.currentDevisId = undefined;
    this.totalTTC = 0;
    this.totalPoidsKg = 0;
    this.updateProgress();
  }

  visualiser(devisId: number): void {
    this.devisService.exportPDF(devisId);
  }
}
