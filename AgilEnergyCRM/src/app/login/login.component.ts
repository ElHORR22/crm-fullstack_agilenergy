
import { AfterViewInit, Component, ElementRef, ViewChild, inject } from '@angular/core';
import { DialogueService } from '../shared/dialogue.service';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../service/auth.service';
import { Utilisateur } from '../modeles/utilisateur.model';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, RouterModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})

export class LoginComponent implements AfterViewInit {
  private readonly dialogue = inject(DialogueService);
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
        if (err.status === 401) {
          this.errorMessage = "Email ou mot de passe incorrect.";
        } else if (err.status === 403) {
          this.errorMessage = "Ce compte est désactivé.";
        } else {
          this.errorMessage = "Le serveur ne répond pas. Veuillez réessayer plus tard.";
        }
      }
    });
  }

  async onForgotPassword(event: Event): Promise<void> {
    event.preventDefault();

    const email = await this.dialogue.demander({
      titre: 'Mot de passe oublié',
      message: 'Saisissez l\'adresse email de votre compte : un lien de réinitialisation vous sera envoyé.',
      libelleConfirmer: 'Envoyer le lien',
      champ: { libelle: 'Adresse email', type: 'email', placeholder: 'nom@exemple.tn' }
    });
    if (!email) return;

    if (!this.validateEmail(email)) {
      this.dialogue.erreur("Format d'email invalide.");
      return;
    }
    this.authService.requestPasswordReset(email).subscribe({
      // Le serveur répond de la même façon que l'adresse existe ou non.
      next: () => this.dialogue.succes("Si un compte existe pour " + email + ", un lien de récupération a été envoyé."),
      error: () => this.dialogue.erreur("Le serveur ne répond pas. Veuillez réessayer plus tard.")
    });
  }

  private validateEmail(email: string) {
    return String(email)
      .toLowerCase()
      .match(/^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/);
  }
}
