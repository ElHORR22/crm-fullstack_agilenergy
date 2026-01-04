import { Component, OnInit } from '@angular/core';
import { DevisService } from '../service/devis.service';
import { Devis } from '../modeles/devis.model';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  standalone: true,
  selector: 'app-mesdevis',
  imports: [CommonModule, FormsModule],
  templateUrl: './mesdevis.component.html',
  styleUrls: ['./mesdevis.component.css']
})

export class MesdevisComponent implements OnInit {
  devisClient: Devis[] = [];
  role: string | null = null;

  constructor(private devisService: DevisService) {}

  ngOnInit(): void {
    const userStr = localStorage.getItem('user');
    let userEmail = null;
    if (userStr) {
      const user = JSON.parse(userStr);
      this.role = user.role;
      userEmail = user.email;
    }
    if (userEmail) {
      this.devisService.getDevisByEmail(userEmail).subscribe({
        next: data => {
          console.log('Devis reçus:', data);
          this.devisClient = data;
        }
      });                 
    }
  }

  visualiser(devisId: number): void {
    this.devisService.exportPDF(devisId);
  }    
  
}


