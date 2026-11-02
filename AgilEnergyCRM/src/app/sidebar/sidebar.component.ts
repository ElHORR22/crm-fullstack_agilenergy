import { Component, HostListener, OnDestroy, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NavigationEnd, Router, RouterModule } from '@angular/router';
import { filter, Subscription } from 'rxjs';
import { AuthService } from '../service/auth.service';
import { Utilisateur } from '../modeles/utilisateur.model';

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.css'],
  imports: [CommonModule, RouterModule]
})

export class SidebarComponent implements OnInit, OnDestroy {

  role: string | null = null;
  user: Utilisateur | null = null;
  // Sidebar ouverte par le bouton menu (sinon elle ne s'affiche qu'au survol).
  ouvert = false;

  private navigationSub?: Subscription;

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  // Relit la session à chaque navigation (connexion, déconnexion) plutôt qu'à chaque
  // cycle de détection de changements, comme le faisait ngDoCheck.
  ngOnInit(): void {
    this.updateUserFromStorage();
    this.navigationSub = this.router.events
      .pipe(filter(event => event instanceof NavigationEnd))
      .subscribe(() => this.updateUserFromStorage());
  }

  ngOnDestroy(): void {
    this.navigationSub?.unsubscribe();
  }

  updateUserFromStorage(): void {
    const userStr = localStorage.getItem('user');
    if (userStr) {
      const utilisateur = JSON.parse(userStr);
      this.user = utilisateur;
      this.role = utilisateur.role;
    } else {
      this.user = null;
      this.role = null;
    }
  }

  basculer(): void {
    this.ouvert = !this.ouvert;
  }

  fermer(): void {
    this.ouvert = false;
  }

  @HostListener('document:keydown.escape')
  fermerAuClavier(): void {
    this.fermer();
  }

  logout() {
    this.fermer();
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
