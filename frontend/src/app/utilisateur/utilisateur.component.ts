import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { OnInit } from '@angular/core';

@Component({
  selector: 'app-utilisateur',
  standalone: true,
  templateUrl: './utilisateur.component.html',
  styleUrls: ['./utilisateur.component.css'],
  imports: [CommonModule, RouterModule]
})
export class UtilisateurComponent {
  constructor(private router: Router) {}

  modules = [
    { title: 'Prospects', subtitle: 'Gérez vos opportunités commerciales', image: 'assets/Prospects.jpg', route: '/prospect', roles: ['ADMINISTRATEUR', 'COMMERCIAL'] },
    { title: 'Clients', subtitle: 'Suivi et fidélisation de votre clientèle', image: 'assets/clients.jpg', route: '/client', roles: ['ADMINISTRATEUR', 'COMMERCIAL'] },
    { title: 'Produits', subtitle: 'Votre catalogue produits', image: 'assets/Produit.jpg', route: '/produit',roles: ['ADMINISTRATEUR', 'COMMERCIAL', 'LOGISTICIEN'] },
    { title: 'Emballages', subtitle: 'Gestion logistique des emballages', image: 'assets/Emballage.jpg', route: '/emballage', roles: ['ADMINISTRATEUR', 'COMMERCIAL', 'LOGISTICIEN'] },
    { title: 'Devis', subtitle: 'Générez et suivez vos devis', image: 'assets/devis.jpg', route: '/devis', roles: ['ADMINISTRATEUR', 'COMMERCIAL'] },
    { title: 'Paramétrage', subtitle: 'Administration et configuration', image: 'assets/Parametrage.jpg', route: '/parametrage', roles: ['ADMINISTRATEUR'] },
    { title: 'Dashboard', subtitle: 'Analyse décisionnelle', image: 'assets/Dashboard.jpg', route: '/dashboard', roles: ['ADMINISTRATEUR'] },
    { title: 'Utilisateurs', subtitle: 'Gérer les comptes utilisateurs', image: 'assets/Utilisateurs.jpg', route: '/gestion_utilisateur',roles: ['ADMINISTRATEUR']},
    { title: 'Mes Devis', subtitle: 'Vos devis personnalisés', image: 'assets/mesdevis.jpg', route: '/mesdevis',roles: ['CLIENT']}
  ];

  displayedModules: {
    title: string;
    subtitle: string;
    image: string;
    route: string;
    roles: string[];
  }[] = [];

  navigate(route: string): void {
    this.router.navigate([route]);
  }

  ngOnInit(): void {
    const userRole = (localStorage.getItem('role') || '').toUpperCase();
    this.displayedModules = this.modules.filter(m => m.roles.includes(userRole));
  }   

}
