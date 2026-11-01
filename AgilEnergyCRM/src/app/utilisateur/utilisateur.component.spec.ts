import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { provideRouter } from '@angular/router';

import { UtilisateurComponent } from './utilisateur.component';

describe('UtilisateurComponent', () => {
  let component: UtilisateurComponent;
  let fixture: ComponentFixture<UtilisateurComponent>;

  beforeEach(async () => {
    // Composant standalone : il s'importe (et non se déclare) ; HttpClient et Router sont simulés.
    await TestBed.configureTestingModule({
      imports: [UtilisateurComponent],
      providers: [provideHttpClient(), provideHttpClientTesting(), provideRouter([])]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UtilisateurComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
