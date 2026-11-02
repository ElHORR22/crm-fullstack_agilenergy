import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { provideRouter } from '@angular/router';

import { DevisComponent } from './devis.component';
import { Devis } from '../modeles/devis.model';

describe('DevisComponent', () => {
  let component: DevisComponent;
  let fixture: ComponentFixture<DevisComponent>;

  beforeEach(async () => {
    // Composant standalone : il s'importe (et non se déclare) ; HttpClient et Router sont simulés.
    await TestBed.configureTestingModule({
      imports: [DevisComponent],
      providers: [provideHttpClient(), provideHttpClientTesting(), provideRouter([])]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DevisComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('applique la TVA paramétrée à un client non exonéré', () => {
    component.tvaValue = 19;
    component.isClientSelected = true;
    component.devisForm.patchValue({ client: { id: 1, exonere: false } });
    expect(component.tauxTvaEffectif()).toBe(19);
  });

  it("n'applique pas de TVA à un client exonéré sans date limite", () => {
    component.tvaValue = 19;
    component.isClientSelected = true;
    component.devisForm.patchValue({ client: { id: 1, exonere: true } });
    expect(component.tauxTvaEffectif()).toBe(0);
  });

  it('ne permet de modifier que les brouillons et de supprimer que brouillons et refusés', () => {
    expect(component.estModifiable({ statut: 'BROUILLON' } as Devis)).toBeTrue();
    expect(component.estModifiable({ statut: 'ENVOYE' } as Devis)).toBeFalse();
    expect(component.estSupprimable({ statut: 'REFUSE' } as Devis)).toBeTrue();
    expect(component.estSupprimable({ statut: 'ACCEPTE' } as Devis)).toBeFalse();
  });

  it('filtre la liste par statut', () => {
    component.filteredDevis = [
      { id: 1, statut: 'BROUILLON' } as Devis,
      { id: 2, statut: 'ACCEPTE' } as Devis,
      { id: 3 } as Devis // ancien devis sans statut = brouillon
    ];
    component.statutFiltre = 'BROUILLON';
    expect(component.devisAffiches.map(d => d.id)).toEqual([1, 3]);
    component.statutFiltre = '';
    expect(component.devisAffiches.length).toBe(3);
  });
});
