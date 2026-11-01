import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { provideRouter } from '@angular/router';

import { NonautoriseComponent } from './nonautorise.component';

describe('NonautoriseComponent', () => {
  let component: NonautoriseComponent;
  let fixture: ComponentFixture<NonautoriseComponent>;

  beforeEach(async () => {
    // Composant standalone : il s'importe (et non se déclare) ; HttpClient et Router sont simulés.
    await TestBed.configureTestingModule({
      imports: [NonautoriseComponent],
      providers: [provideHttpClient(), provideHttpClientTesting(), provideRouter([])]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NonautoriseComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
