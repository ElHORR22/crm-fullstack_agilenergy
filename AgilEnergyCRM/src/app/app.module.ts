import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms'; 
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { AuthInterceptor } from './interceptors/auth.interceptor';
import { ClientComponent } from './client/client.component';
import { FilterPipe } from './client/Filtre';
import { ClientService } from './service/client.service';
import { CommonModule } from '@angular/common';
import { FiltrePipe } from "./produit/FiltreProduit";
import { NgChartsModule } from 'ng2-charts';
import { SidebarComponent } from './sidebar/sidebar.component';
import { GestionUtilisateurComponent } from './gestion-utilisateurs/gestion-utilisateurs.component';

@NgModule({
  declarations: [AppComponent],
  imports: [BrowserModule, SidebarComponent, GestionUtilisateurComponent, AppRoutingModule, FormsModule, HttpClientModule, ClientComponent, NgChartsModule, FilterPipe, CommonModule, FiltrePipe, SidebarComponent],
  providers: [ClientService, { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true }],
  bootstrap: [AppComponent]
})
export class AppModule { }
