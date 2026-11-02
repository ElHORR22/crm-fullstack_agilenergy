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
    if (userStr) {
      this.role = JSON.parse(userStr).role;
    }
    this.devisService.getMesDevis().subscribe({
      next: data => this.devisClient = data
    });
  }

  visualiser(devisId: number): void {
    this.devisService.downloadPDF(devisId);
  }
  
}


