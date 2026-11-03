import { TestBed } from '@angular/core/testing';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { provideNoopAnimations } from '@angular/platform-browser/animations';
import { ConfirmDialogComponent, ConfirmDialogData } from './confirm-dialog.component';

describe('ConfirmDialogComponent', () => {
  let close: jasmine.Spy;

  function creer(data: ConfirmDialogData): ConfirmDialogComponent {
    close = jasmine.createSpy('close');
    TestBed.configureTestingModule({
      imports: [ConfirmDialogComponent],
      providers: [
        provideNoopAnimations(),
        { provide: MAT_DIALOG_DATA, useValue: data },
        { provide: MatDialogRef, useValue: { close } }
      ]
    });
    const fixture = TestBed.createComponent(ConfirmDialogComponent);
    fixture.detectChanges();
    return fixture.componentInstance;
  }

  it('renvoie true à la confirmation et false à l\'annulation', () => {
    const dialog = creer({ titre: 'Supprimer', message: 'Sûr ?' });
    dialog.valider();
    expect(close).toHaveBeenCalledWith(true);
    dialog.annuler();
    expect(close).toHaveBeenCalledWith(false);
  });

  it('renvoie le texte saisi (sans espaces) en mode saisie, null si annulé', () => {
    const dialog = creer({ titre: 'Email', message: 'Votre email', champ: { libelle: 'Email' } });
    dialog.valeur = '  a@b.tn ';
    dialog.valider();
    expect(close).toHaveBeenCalledWith('a@b.tn');
    dialog.annuler();
    expect(close).toHaveBeenCalledWith(null);
  });

  it('ne valide pas une saisie vide', () => {
    const dialog = creer({ titre: 'Email', message: 'Votre email', champ: { libelle: 'Email' } });
    dialog.valeur = '   ';
    dialog.valider();
    expect(close).not.toHaveBeenCalled();
  });
});
