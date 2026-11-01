import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { provideRouter } from '@angular/router';

import { GestionUtilisateurComponent } from './gestion-utilisateurs.component';

describe('GestionUtilisateurComponent', () => {
  let component: GestionUtilisateurComponent;
  let fixture: ComponentFixture<GestionUtilisateurComponent>;

  beforeEach(async () => {
    // Composant standalone : il s'importe (et non se déclare) ; HttpClient et Router sont simulés.
    await TestBed.configureTestingModule({
      imports: [GestionUtilisateurComponent],
      providers: [provideHttpClient(), provideHttpClientTesting(), provideRouter([])]
    })
    .compileComponents();

    fixture = TestBed.createComponent(GestionUtilisateurComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
