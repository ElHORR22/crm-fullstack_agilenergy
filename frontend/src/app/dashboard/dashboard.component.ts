import { Component, OnInit } from '@angular/core';
import { ClientsProspectsCount, DashboardService, MoisDevis, ProduitDemandes } from '../service/dashboard.service';
import { CommonModule } from '@angular/common';
import { NgChartsModule } from 'ng2-charts';
import { Chart, ArcElement, Tooltip, Legend, PieController, DoughnutController, ChartDataset } from 'chart.js';
Chart.register(ArcElement, Tooltip, Legend, PieController, DoughnutController);
import { ChartOptions, ChartConfiguration, ChartType } from 'chart.js';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css'],
  imports: [CommonModule, NgChartsModule]
})
export class DashboardComponent implements OnInit {

  role: string | null = null;

  chiffreAffairesCurrentMonth = 0;
  chiffreAffairesPreviousMonth = 0;

  chiffreAffaires: number = 0;
  gouvernoratChartLabels: string[] = [];
  gouvernoratChartData: number[] = [];

  secteurChartLabels: string[] = [];
  secteurChartData: number[] = [];

  periode: string = '';
  caEvolution = 0;

  showDetails: boolean = false;
  showPopup = false;

  chartOptions: ChartOptions<'pie' | 'doughnut'> = {
    responsive: true,
    plugins: {
      legend: {
        display: true,
        position: 'bottom',
        labels: {
          font: { size: 14, weight: 'bold' },
          color: '#4a4a4a'
        }
      },
      tooltip: {
        enabled: true,
        backgroundColor: '#172b4d',
        titleFont: { size: 16, weight: 'bold' },
        bodyFont: { size: 14 }
      }
    }
  };

  chartColors = ['#007bff', '#00c6ff', '#00e396', '#feb019', '#ff4560', '#775dd0', '#4b6584'];

  public popupChartType: ChartType = 'bar';

  public popupChartData: ChartConfiguration<'bar'>['data'] = {
    labels: ['Mois précédent', 'Mois actuel'],
    datasets: [
      {
        data: [],
        label: 'Chiffre d\'affaires (TND)',
        backgroundColor: ['#3498db', '#2ecc71']
      }
    ]
  };

  barChartOptions: ChartOptions = { responsive: true };
  barChartLabels: string[] = [];
  barChartType: ChartType = 'bar';
  barChartLegend = false;
  barChartData: ChartDataset[] = [
    { data: [], label: 'Nombre de devis' }
  ];

  topProduitsLabels: string[] = [];
  topProduitsData: number[] = [];

  clientsProspectsChartLabels: string[] = ['Clients', 'Prospects'];
  clientsProspectsChartData = [{ data: [0, 0], backgroundColor: ['#1a73e8', '#e0f7fa'], borderWidth: 0 }];
  clientsProspectsChartType: ChartType = 'doughnut';
  clientsProspectsChartOptions: ChartOptions<'doughnut'> = {
    responsive: true,
    plugins: {
      legend: { 
        position: 'bottom', 
        labels: { font: { size: 14, weight: 'bold' }, color: '#555' } 
      },
      tooltip: {
        enabled: true,
        callbacks: {
          label: (context) => {
            const label = context.label || '';
            const value = context.parsed;
            const data = context.chart.data.datasets[0].data as number[];
            const total = data.reduce((sum, val) => sum + val, 0);
            const percent = total ? ((value / total) * 100).toFixed(1) : '0';
            return `${label}: ${value} (${percent}%)`;
          }
        }
      }
    }
  };  

  constructor(private dashboardService: DashboardService ) { }

  ngOnInit(): void {
    this.dashboardService.getChiffreAffaires().subscribe(data => {
      this.chiffreAffaires = data;
    });

    this.dashboardService.getClientsParGouvernorat().subscribe(data => {
      this.gouvernoratChartLabels = data.map(item => item[0]);
      this.gouvernoratChartData = data.map(item => item[1]);
    });

    this.dashboardService.getClientsParSecteur().subscribe(data => {
      this.secteurChartLabels = data.map(item => item[0]);
      this.secteurChartData = data.map(item => item[1]);
    });

    this.chargerChiffreAffairesEtEvolution();
    this.loadChiffreAffaires();
    this.loadDevisParMois();
    this.loadTopProduits();
    this.loadClientsProspectsCount();
  }

  scrollTo(section: string) {
    const element = document.getElementById(section);
    if (element) {
      element.scrollIntoView({ behavior: 'smooth' });
    } else {
      console.error(`Section ${section} introuvable.`);
    }
  }

  voirDetails(): void {
    const now = new Date();
    const annee = now.getFullYear();
    const mois = now.getMonth() + 1;
  
    let anneePrecedente = annee;
    let moisPrecedent = mois - 1;
    if (moisPrecedent === 0) {
      moisPrecedent = 12;
      anneePrecedente--;
    }
  
    this.dashboardService.getCaMensuel(annee, mois).subscribe(caActuel => {
      this.dashboardService.getCaMensuel(anneePrecedente, moisPrecedent).subscribe(caPrecedent => {
        this.popupChartData.datasets[0].data = [caPrecedent || 0, caActuel || 0];
        this.showPopup = true;
      });
    });
  }  

  chargerChiffreAffairesEtEvolution(): void {
    const now = new Date();
    const annee = now.getFullYear();
    const mois = now.getMonth() + 1;
  
    const previousMonth = mois === 1 ? 12 : mois - 1;
    const previousYear = mois === 1 ? annee - 1 : annee;
  
    this.dashboardService.getCaMensuel(annee, mois).subscribe(caCurrent => {
      this.chiffreAffairesCurrentMonth = caCurrent ?? 0;
  
      this.dashboardService.getCaMensuel(previousYear, previousMonth).subscribe(caPrevious => {
        this.chiffreAffairesPreviousMonth = caPrevious ?? 0;
  
        if (this.chiffreAffairesPreviousMonth === 0) {
          this.caEvolution = 100; 
        } else {
          this.caEvolution = ((this.chiffreAffairesCurrentMonth - this.chiffreAffairesPreviousMonth) / this.chiffreAffairesPreviousMonth) * 100;
        }
  
        this.popupChartData.datasets[0].data = [this.chiffreAffairesPreviousMonth, this.chiffreAffairesCurrentMonth];
  
        this.periode = `Mois ${mois} / ${annee}`;
      });
    });
  }  

  loadChiffreAffaires() {
    this.chiffreAffairesCurrentMonth = 13450.75;
    this.chiffreAffairesPreviousMonth = 12000.00;

    this.caEvolution = ((this.chiffreAffairesCurrentMonth - this.chiffreAffairesPreviousMonth) / this.chiffreAffairesPreviousMonth) * 100;
  }

  toggleDetails() {
    this.showDetails = !this.showDetails;
  }

  loadDevisParMois(): void {
    this.dashboardService.getDevisParMois().subscribe(data => {
      this.barChartLabels = data.map(d => d.mois);
      this.barChartData[0].data = data.map(d => d.nbDevis);
    });
  }

  loadTopProduits(): void {
    this.dashboardService.getTopProduits().subscribe(data => {
      this.topProduitsLabels = data.map(p => p.codeProduit);
      this.topProduitsData = data.map(p => p.quantite);
    });
  }
  
  loadClientsProspectsCount(): void {
    this.dashboardService.getClientsProspectsCount().subscribe({
      next: (data: ClientsProspectsCount) => {
        this.clientsProspectsChartData[0].data = [data.clients, data.prospects];
      },
      error: (err: any) => {
        console.error('Erreur chargement clients/prospects:', err);
      }
    });
  }
}
