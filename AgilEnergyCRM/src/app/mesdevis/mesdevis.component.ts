import { Component, OnInit, inject } from '@angular/core';
import { DialogueService } from '../shared/dialogue.service';
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
  private readonly dialogue = inject(DialogueService);
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

  async repondre(devis: Devis, reponse: 'ACCEPTE' | 'REFUSE'): Promise<void> {
    const verbe = reponse === 'ACCEPTE' ? 'Accepter' : 'Refuser';
    const ok = await this.dialogue.confirmer({
      titre: `${verbe} le devis #${devis.id}`,
      message: `« ${devis.sujetDevis} » — ce choix est définitif.`,
      libelleConfirmer: verbe,
      danger: reponse === 'REFUSE'
    });
    if (!ok) return;

    this.devisService.repondre(devis.id!, reponse).subscribe({
      next: () => {
        this.dialogue.succes(reponse === 'ACCEPTE' ? 'Devis accepté, merci !' : 'Devis refusé.');
        this.chargerDevis();
      },
      error: (err) => this.dialogue.erreur(err?.error?.message ?? 'Impossible d\'enregistrer votre réponse.')
    });
  }
}
