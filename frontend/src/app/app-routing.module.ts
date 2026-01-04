import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AcceuilComponent } from './acceuil/acceuil.component';
import { LoginComponent } from './login/login.component';
import { UtilisateurComponent } from './utilisateur/utilisateur.component';
import { ClientComponent } from './client/client.component';
import { ProspectComponent } from './prospect/prospect.component';
import { EmballageComponent } from './emballage/emballage.component';
import { ProduitComponent } from './produit/produit.component';
import { DevisComponent } from './devis/devis.component';
import { ParametrageComponent } from './parametrage/parametrage.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { AuthGuard } from './login/auth.guard';
import { MesdevisComponent } from './mesdevis/mesdevis.component';
import { GestionUtilisateurComponent } from './gestion-utilisateurs/gestion-utilisateurs.component';

const routes: Routes = [
  { path: 'acceuil', component: AcceuilComponent },
  { path: 'login', component: LoginComponent },
  { path: 'utilisateur', component: UtilisateurComponent },
  { path: 'client', component: ClientComponent, canActivate: [AuthGuard], data: { roles: ['ADMINISTRATEUR', 'COMMERCIAL'] } },
  { path: 'prospect', component: ProspectComponent, canActivate: [AuthGuard], data: { roles: ['ADMINISTRATEUR', 'COMMERCIAL'] } },
  { path: 'emballage', component: EmballageComponent, canActivate: [AuthGuard], data: { roles: ['ADMINISTRATEUR', 'COMMERCIAL', 'LOGISTICIEN'] } },
  { path: 'produit', component: ProduitComponent, canActivate: [AuthGuard], data: { roles: ['ADMINISTRATEUR', 'COMMERCIAL', 'LOGISTICIEN'] } },
  { path: 'devis', component: DevisComponent, canActivate: [AuthGuard], data: { roles: ['COMMERCIAL', 'ADMINISTRATEUR'] } },
  { path: 'parametrage', component: ParametrageComponent, canActivate: [AuthGuard], data: { roles: ['ADMINISTRATEUR'] } },
  { path: 'dashboard', component: DashboardComponent, canActivate: [AuthGuard], data: { roles: ['COMMERCIAL', 'ADMINISTRATEUR'] } },
  { path: 'mesdevis', component: MesdevisComponent, canActivate: [AuthGuard], data: { roles: ['CLIENT'] }},
  { path: 'gestion_utilisateur', component: GestionUtilisateurComponent, canActivate: [AuthGuard], data: { roles: ['ADMINISTRATEUR'] } },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
