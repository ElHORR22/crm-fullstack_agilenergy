import { Component, OnInit } from '@angular/core';
import { DevisService } from '../service/devis.service';
import { Devis, LIBELLES_STATUT } from '../modeles/devis.model';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  standalone: true,
  selector: 'app-mesdevis',
  imports: [CommonModule, FormsModule],
  templateUrl: './mesdevis.component.html',
  styleUrls: ['./mesdevis.component.css']
})

export class MesdevisComponent implements OnInit {
  devisClient: Devis[] = [];
  role: string | null = null;
  readonly libellesStatut = LIBELLES_STATUT;

  constructor(private devisService: DevisService) {}

  ngOnInit(): void {
    const userStr = localStorage.getItem('user');
    if (userStr) {
      this.role = JSON.parse(userStr).role;
    }
    this.chargerDevis();
  }

  chargerDevis(): void {
    this.devisService.getMesDevis().subscribe({
      next: data => this.devisClient = data
    });
  }

  visualiser(devisId: number): void {
    this.devisService.downloadPDF(devisId);
  }

  repondre(devis: Devis, reponse: 'ACCEPTE' | 'REFUSE'): void {
    const verbe = reponse === 'ACCEPTE' ? 'accepter' : 'refuser';
    if (!confirm(`Voulez-vous ${verbe} le devis #${devis.id} « ${devis.sujetDevis} » ? Ce choix est définitif.`)) return;

    this.devisService.repondre(devis.id!, reponse).subscribe({
      next: () => this.chargerDevis(),
      error: (err) => alert(err?.error?.message ?? 'Impossible d\'enregistrer votre réponse.')
    });
  }
}
