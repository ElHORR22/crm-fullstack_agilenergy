import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { RouterModule } from '@angular/router';
import { AppComponent } from './app.component';
import { SidebarComponent } from './sidebar/sidebar.component';

describe('AppComponent', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      // AppComponent n'est pas standalone : RouterModule fournit <router-outlet> et routerLink.
      declarations: [AppComponent],
      imports: [RouterModule.forRoot([]), SidebarComponent],
      providers: [provideHttpClient(), provideHttpClientTesting()]
    }).compileComponents();
  });

  it('should create the app', () => {
    const fixture = TestBed.createComponent(AppComponent);
    expect(fixture.componentInstance).toBeTruthy();
  });

  it(`should have as title 'StagePFE'`, () => {
    const fixture = TestBed.createComponent(AppComponent);
    expect(fixture.componentInstance.title).toEqual('StagePFE');
  });
});
