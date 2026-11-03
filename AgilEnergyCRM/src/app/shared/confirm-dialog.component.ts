import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';

export interface ConfirmDialogData {
  titre: string;
  message: string;
  libelleConfirmer?: string;
  // Action irréversible (suppression…) : bouton de confirmation en rouge.
  danger?: boolean;
  // Si renseigné, la boîte demande une saisie (remplace prompt()) et renvoie le texte.
  champ?: { libelle: string; type?: string; placeholder?: string };
}

// Boîte de dialogue unique pour les confirmations et les saisies simples,
// à la place de confirm() et prompt() du navigateur.
@Component({
  selector: 'app-confirm-dialog',
  standalone: true,
  imports: [FormsModule, MatDialogModule, MatButtonModule, MatFormFieldModule, MatInputModule],
  template: `
    <h2 mat-dialog-title>{{ data.titre }}</h2>
    <mat-dialog-content>
      <p class="confirm-message">{{ data.message }}</p>
      @if (data.champ) {
        <mat-form-field appearance="outline" class="confirm-champ">
          <mat-label>{{ data.champ.libelle }}</mat-label>
          <input matInput [type]="data.champ.type ?? 'text'" [placeholder]="data.champ.placeholder ?? ''"
            [(ngModel)]="valeur" (keyup.enter)="valider()" cdkFocusInitial />
        </mat-form-field>
      }
    </mat-dialog-content>
    <mat-dialog-actions align="end">
      <button mat-button type="button" (click)="annuler()">Annuler</button>
      <button mat-flat-button type="button" [class.bouton-danger]="data.danger"
        [disabled]="!!data.champ && !valeur.trim()" (click)="valider()">
        {{ data.libelleConfirmer ?? 'Confirmer' }}
      </button>
    </mat-dialog-actions>
  `,
  styles: [`
    .confirm-message { margin: 0 0 12px; line-height: 1.5; }
    .confirm-champ { width: 100%; }
    .bouton-danger { --mdc-filled-button-container-color: #c62828; }
  `]
})
export class ConfirmDialogComponent {
  readonly data = inject<ConfirmDialogData>(MAT_DIALOG_DATA);
  private readonly ref = inject(MatDialogRef<ConfirmDialogComponent, boolean | string>);
  valeur = '';

  annuler(): void {
    this.ref.close(this.data.champ ? null : false);
  }

  valider(): void {
    if (this.data.champ) {
      if (this.valeur.trim()) this.ref.close(this.valeur.trim());
    } else {
      this.ref.close(true);
    }
  }
}
