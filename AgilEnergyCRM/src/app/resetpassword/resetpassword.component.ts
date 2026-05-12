import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { AuthService } from '../service/auth.service';

@Component({
    selector: 'app-reset-password',
    standalone: true,
    imports: [CommonModule, FormsModule, RouterModule],
    templateUrl: './resetpassword.component.html',
    styleUrls: ['./resetpassword.component.css']
})
export class ResetPasswordComponent implements OnInit {
    token: string = '';
    newPassword: string = '';
    confirmPassword: string = ''; // Ajouté pour la sécurité
    message: string = '';

    constructor(
        private route: ActivatedRoute,
        private authService: AuthService,
        private router: Router
    ) { }

    ngOnInit(): void {
        this.token = this.route.snapshot.queryParamMap.get('token') || '';
    }

    onSubmit(): void {
        if (this.newPassword !== this.confirmPassword) {
            this.message = "Les mots de passe ne correspondent pas.";
            return;
        }

        this.authService.resetPassword(this.token, this.newPassword).subscribe({
            next: () => {
                alert("Succès ! Votre mot de passe a été réinitialisé.");
                this.router.navigate(['/login']);
            },
            error: (err) => {
                this.message = "Le lien est invalide ou a expiré. Veuillez refaire une demande.";
            }
        });
    }
}