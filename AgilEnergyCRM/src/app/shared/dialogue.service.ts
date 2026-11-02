import { Injectable, inject } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { firstValueFrom } from 'rxjs';
import { ConfirmDialogComponent, ConfirmDialogData } from './confirm-dialog.component';

// Remplace alert(), confirm() et prompt() : notifications discrètes et boîtes de dialogue
// cohérentes avec le reste de l'interface, qui ne bloquent pas la page.
@Injectable({ providedIn: 'root' })
export class DialogueService {
  private readonly dialog = inject(MatDialog);
  private readonly snackBar = inject(MatSnackBar);

  succes(message: string): void {
    this.snackBar.open(message, 'OK', { duration: 3500, panelClass: 'notification-succes' });
  }

  erreur(message: string): void {
    this.snackBar.open(message, 'Fermer', { duration: 6000, panelClass: 'notification-erreur' });
  }

  // Résout à true si l'utilisateur confirme.
  async confirmer(data: Omit<ConfirmDialogData, 'champ'>): Promise<boolean> {
    const ref = this.dialog.open(ConfirmDialogComponent, { data, width: '440px', maxWidth: '92vw' });
    return (await firstValueFrom(ref.afterClosed())) === true;
  }

  // Résout au texte saisi, ou null si l'utilisateur annule.
  async demander(data: ConfirmDialogData & { champ: NonNullable<ConfirmDialogData['champ']> }): Promise<string | null> {
    const ref = this.dialog.open(ConfirmDialogComponent, { data, width: '440px', maxWidth: '92vw' });
    const resultat = await firstValueFrom(ref.afterClosed());
    return typeof resultat === 'string' ? resultat : null;
  }
}
