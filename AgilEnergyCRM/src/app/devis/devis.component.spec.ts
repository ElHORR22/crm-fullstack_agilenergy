import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { provideRouter } from '@angular/router';

import { DevisComponent } from './devis.component';

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
});
