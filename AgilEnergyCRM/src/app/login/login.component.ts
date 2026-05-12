import { CommonModule } from '@angular/common';
import { AfterViewInit, Component, ElementRef, ViewChild } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../service/auth.service';
import { Utilisateur } from '../modeles/utilisateur.model';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})

export class LoginComponent implements AfterViewInit {
  utilisateur: Utilisateur = { email: '', mdp: '' };
  errorMessage = '';

  constructor(private router: Router, private authService: AuthService) { }

  @ViewChild('backgroundVideo', { static: false }) videoRef!: ElementRef<HTMLVideoElement>;

  ngAfterViewInit(): void {
    const video = this.videoRef.nativeElement;
    video.muted = true;
    video.setAttribute('muted', '');
    video.play().catch((e) => {
      console.warn('Autoplay bloqué :', e);
    });
  }

  scrollTo(section: string) {
    const element = document.getElementById(section);
    if (element) {
      element.scrollIntoView({ behavior: 'smooth' });
    }
  }

  onSubmit(): void {
    this.authService.login({
      email: this.utilisateur.email,
      mdp: this.utilisateur.mdp
    }).subscribe({
      next: (res: any) => {
        localStorage.setItem('user', JSON.stringify(res));
        localStorage.setItem('role', (res.role || '').toUpperCase());

        if (res.token) {
          localStorage.setItem('token', res.token);
        }

        this.router.navigate(['/utilisateur']);
      },
      error: err => {
        if (err.status === 401 || err.status === 403) {
          this.router.navigate(['/nonautorise']);
        } else {
          this.errorMessage = "Le serveur ne répond pas. Veuillez réessayer plus tard.";
        }
      }
    });
  }

  onForgotPassword(event: Event): void {
    event.preventDefault();

    const email = prompt("Entrez votre adresse email :");

    if (email && this.validateEmail(email)) {
      this.authService.requestPasswordReset(email).subscribe({
        next: () => alert("Un lien de récupération a été envoyé à " + email),
        error: (err) => alert("Erreur : " + (err.error?.message || "Email non trouvé."))
      });
    } else if (email) {
      alert("Format d'email invalide.");
    }
  }

  private validateEmail(email: string) {
    return String(email)
      .toLowerCase()
      .match(/^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/);
  }
}
