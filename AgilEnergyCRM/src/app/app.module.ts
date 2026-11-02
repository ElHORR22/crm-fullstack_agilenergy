import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { FormsModule } from '@angular/forms';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HTTP_INTERCEPTORS, provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';
import { AuthInterceptor } from './interceptors/auth.interceptor';
import { CommonModule } from '@angular/common';
import { NgChartsModule } from 'ng2-charts';
import { SidebarComponent } from './sidebar/sidebar.component';

// Les pages sont des composants standalone chargés par le routeur :
// seul ce qui apparaît dans le template d'AppComponent est importé ici.
@NgModule({
  declarations: [AppComponent],
  imports: [BrowserModule, CommonModule, FormsModule, AppRoutingModule, NgChartsModule, SidebarComponent],
  providers: [
    provideAnimationsAsync(), // animations des dialogues et notifications Angular Material
    provideHttpClient(withInterceptorsFromDi()),
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
