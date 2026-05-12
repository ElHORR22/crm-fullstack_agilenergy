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
import { authGuard } from './login/auth.guard';
import { MesdevisComponent } from './mesdevis/mesdevis.component';
import { GestionUtilisateurComponent } from './gestion-utilisateurs/gestion-utilisateurs.component';
import { NonautoriseComponent } from './nonautorise/nonautorise.component';
import { ResetPasswordComponent } from './resetpassword/resetpassword.component';

const routes: Routes = [
  { path: 'acceuil', component: AcceuilComponent },
  { path: 'login', component: LoginComponent },
  { path: 'utilisateur', component: UtilisateurComponent },
  { path: 'client', component: ClientComponent, canActivate: [authGuard], data: { roles: ['ADMINISTRATEUR', 'COMMERCIAL'] } },
  { path: 'prospect', component: ProspectComponent, canActivate: [authGuard], data: { roles: ['ADMINISTRATEUR', 'COMMERCIAL'] } },
  { path: 'emballage', component: EmballageComponent, canActivate: [authGuard], data: { roles: ['ADMINISTRATEUR', 'COMMERCIAL', 'LOGISTICIEN'] } },
  { path: 'produit', component: ProduitComponent, canActivate: [authGuard], data: { roles: ['ADMINISTRATEUR', 'COMMERCIAL', 'LOGISTICIEN'] } },
  { path: 'devis', component: DevisComponent, canActivate: [authGuard], data: { roles: ['COMMERCIAL', 'ADMINISTRATEUR'] } },
  { path: 'parametrage', component: ParametrageComponent, canActivate: [authGuard], data: { roles: ['ADMINISTRATEUR'] } },
  { path: 'dashboard', component: DashboardComponent, canActivate: [authGuard], data: { roles: ['COMMERCIAL', 'ADMINISTRATEUR'] } },
  { path: 'mesdevis', component: MesdevisComponent, canActivate: [authGuard], data: { roles: ['CLIENT'] } },
  { path: 'gestion_utilisateur', component: GestionUtilisateurComponent, canActivate: [authGuard], data: { roles: ['ADMINISTRATEUR'] } },
  { path: 'nonautorise', component: NonautoriseComponent },
  { path: 'reset-password', component: ResetPasswordComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
