import { Component, DoCheck, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../service/auth.service';
import { Utilisateur } from '../modeles/utilisateur.model';

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.css'],
  imports: [CommonModule, RouterModule]
})

export class SidebarComponent implements OnInit, DoCheck {

  role: string | null = null;
  user: Utilisateur | null = null;

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.updateUserFromStorage();
  }

  ngDoCheck(): void {
    this.updateUserFromStorage();
  }

  updateUserFromStorage(): void {
    const userStr = localStorage.getItem('user');
    if (userStr) {
      const utilisateur = JSON.parse(userStr);
      if (utilisateur.email !== this.user?.email) {
        this.user = utilisateur;
        this.role = utilisateur.role;
      }
    } else {
      this.user = null;
      this.role = null;
    }
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}

