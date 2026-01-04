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

  constructor(private router: Router, private authService: AuthService) {}

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
    this.authService.login(this.utilisateur).subscribe({
      next: (res: any) => {
        localStorage.setItem('user', JSON.stringify(res));
        localStorage.setItem('role', (res.role || '').toUpperCase());
      
        if (res.token) {
          localStorage.setItem('token', res.token);
        }
      
        this.router.navigate(['/utilisateur']);
      },      
      error: err => {
        console.error("Erreur lors de la connexion :", err);
        this.errorMessage = err.error?.message || "Erreur inconnue.";
      }
    });
  }
        

}
