import { Component, HostListener, OnInit } from '@angular/core';
import { ClientService } from '../service/client.service';
import { ContactService } from '../service/contact.service';
import { Client, SecteurActivite, Gouvernorat } from '../modeles/client.model';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { FilterPipe } from "./Filtre";
import { Contact } from '../modeles/contact.model';
import { Router, RouterModule } from '@angular/router';

@Component({
  selector: 'app-client',
  standalone: true,
  templateUrl: './client.component.html',
  styleUrls: ['./client.component.css'],
  imports: [FormsModule, CommonModule, FilterPipe, RouterModule] 
})

export class ClientComponent implements OnInit {
  
  openedClient: any;
  progressValue = 0;
  clients: Client[] = [];
  filteredClients: Client[] = [];
  searchTerm: string = '';
  errorMessage: string = '';
  successMessage: string = '';
  newClient: Client = new Client(); 
  showAddForm: boolean = false;
  showEditForm: boolean = false;
  showErrorPopup: boolean = false;
  showDetailPopup: boolean = false;
  showContactSidePanel: boolean = false;
  selectedClientForContacts: Client | null = null;
  selectedClients: Client[] = []; 
  selectedClient: Client | null = null;
  gouvernorats: Gouvernorat[] = [];
  secteurActivite: SecteurActivite[] = [];
  contacts: Contact[] = [];
  showAddFormContact = false;
  showEditFormContact = false;
  showContactList: boolean = false;
  selectedContact?: Contact;
  clientId!: number;

  newContact: Contact = {
    nom: '',
    prenom: '',
    fonction: '',
    clientId: 0,
    mobile: 0,
    telephone: 0,
    fax: 0,
    email: '',
    emailSecondaire: '',
    siteWeb: '',
    adresse: '',
    codePostal: 0,
    ville: '',
    pays: '',
  };

  constructor(private clientService: ClientService, private contactService: ContactService, private router: Router) { }

  ngOnInit(): void {
    this.loadClients();
    this.loadGouvernorats();  
    this.loadSecteurActivite();
    this.loadContacts(this.clientId)
  }

  updateProgress() {
    let filled = 0;
    const total = 20;
  
    const c = this.newClient;
  
    if (c.nom) filled++;
    if (c.numCompte) filled++;
    if (c.numsousCompte) filled++;
    if (c.matFiscal) filled++;
    if (c.chiffreA) filled++;
    if (c.effectif) filled++;
    if (c.email) filled++;
    if (c.autremail) filled++;
    if (c.siteWeb) filled++;
    if (c.telephone) filled++;
    if (c.autretelephone) filled++;
    if (c.fax) filled++;
    if (c.adresse) filled++;
    if (c.codePostal) filled++;
    if (c.ville) filled++;
    if (c.pays) filled++;
    if (c.conditionPaiement) filled++;
    if (c.typeprixAchat) filled++;
    if (c.gouvernorat?.id) filled++;
    if (c.secteurActivite?.id) filled++;
  
    this.progressValue = Math.floor((filled / total) * 100);
  }  

  loadClients(): void {
    this.clientService.getAllClients().subscribe((data) => {
      this.clients = data;
      this.filteredClients = data;  
    });
  }

  loadGouvernorats(): void {
    this.clientService.getGouvernorats().subscribe((data) => {
      this.gouvernorats = data;
    });
  }

  loadSecteurActivite(): void {
    this.clientService.getSecteurActivite().subscribe((data) => {
      this.secteurActivite = data;
    });
  }

  loadContacts(clientId: number) {
    this.contactService.getContactsByClientId(clientId).subscribe((contacts) => {
      this.contacts = contacts;
      this.showContactList = true; 
    });
  }       

  openPopup(client: Client): void {
    this.selectedClient = client;
    this.showDetailPopup = true;
  }

  closePopup(): void {
    this.selectedClient = null;
    this.showDetailPopup = false;
  }

  closeErrorPopup(): void {
    this.showErrorPopup = false;
    this.errorMessage = '';
  } 

  closeContactSidePanel(): void {
    this.showContactSidePanel = false;
  }  
  
  searchClients(): void {
    if (this.searchTerm) {
      this.filteredClients = this.clients.filter(client => 
        client.nom.toLowerCase().includes(this.searchTerm.toLowerCase()) || 
        client.email.toLowerCase().includes(this.searchTerm.toLowerCase()) || 
        client.telephone.toString().includes(this.searchTerm) ||
        client.id?.toString().includes(this.searchTerm)
      );
    } else {
      this.filteredClients = this.clients; 
    }
  }
  
  scrollTo(section: string): void {
    const element = document.getElementById(section);
    if (element) {
      element.scrollIntoView({ behavior: 'smooth' });
    }
  }

  onClientSelect(client: Client, event: any): void {
    const checked = event.target.checked;

    this.filteredClients.forEach(c => {
      c['checked'] = false;
      c['showDropdown'] = false;
    });

    if (checked) {
      client['checked'] = true;
      this.selectedClient = client;
    } else {
      this.selectedClient = null;
    }
  }

  isSelected(client: Client): boolean {
    return this.selectedClients.some(c => c.id === client.id);
  }
  
  toggleAddForm(): void {
    this.showAddForm = !this.showAddForm;
    this.newClient = new Client();
  } 

  addClient(): void {
    if (!this.newClient.nom || !this.newClient.matFiscal || !this.newClient.numCompte || !this.newClient.numsousCompte || !this.newClient.chiffreA ||
        !this.newClient.effectif || !this.newClient.conditionPaiement || !this.newClient.typeprixAchat ||
        !this.newClient.email || !this.newClient.telephone || !this.newClient.adresse || !this.newClient.codePostal || 
        !this.newClient.ville || !this.newClient.pays) {
      this.errorMessage = 'Tous les champs obligatoires doivent être remplis !';
      return;
    }
  
    const nameRegex = /^[a-zA-ZéèêàâîïôùçÉÈÊÀÂÎÏÔÙÇ\-\'\s]+$/;
    if (!nameRegex.test(this.newClient.nom)) {
      alert("Le nom ne doit contenir que des lettres.");
      return;
    }

    const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    if (!emailRegex.test(this.newClient.email)) {
      this.errorMessage = 'L\'adresse email est invalide !';
      return;
    }
  
    const telStr = this.newClient.telephone?.toString().trim();
    if (!/^\d{8}$/.test(telStr)) {
      this.errorMessage = 'Le numéro de téléphone doit contenir exactement 8 chiffres !';
      return;
    }

    const faxStr = this.newClient.fax?.toString().trim();
    if (!/^\d{8}$/.test(faxStr)) {
      this.errorMessage = 'Le numéro de Fax doit contenir exactement 8 chiffres !';
      return;
    }
  
    if (this.newClient.numCompte <= 0 || this.newClient.numsousCompte <= 0) {
      this.errorMessage = 'Les numéros de compte doivent être valides !';
      return;
    }

    this.errorMessage = '';
  
    this.clientService.addClient(this.newClient).subscribe(() => {
      this.loadClients(); 
      this.toggleAddForm(); 
    });
  }
  
  editClient(client: Client): void {
    
    this.selectedClient = { ...client };
  
    this.selectedClient.gouvernorat = this.gouvernorats.find(g => g.id === client.gouvernorat?.id)!;
    this.selectedClient.secteurActivite = this.secteurActivite.find(s => s.id === client.secteurActivite?.id)!;
  
    this.toggleEditForm();
  }
  

  toggleEditForm(): void {
    this.showEditForm = !this.showEditForm;
  }  

  toggleDropdown(client: Client): void {
    if (!client.checked) {
      this.showErrorPopup = true;
      this.errorMessage = "Veuillez d'abord sélectionner un client.";
      return;
    }

    this.filteredClients.forEach(p => {
      p['showDropdown'] = false;
    });

    client['showDropdown'] = true;
  }

  @HostListener('document:click', ['$event'])
    onDocumentClick(event: MouseEvent): void {
      const target = event.target as HTMLElement;
      if (!target.closest('.actions-container')) {
        this.filteredClients.forEach(client => {
         client['showDropdown'] = false;
      });
    }
  }

  toggleAddFormContact(client?: Client) {
    this.showAddFormContact = !this.showAddFormContact;
    if (client) {
      this.selectedClient = client;
    }
    this.newContact = {
      id: undefined,
      nom: '',
      prenom: '',
      fonction: '',
      clientId: this.selectedClient?.id || 0,
      mobile: 0,
      telephone: 0,
      fax: 0,
      email: '',
      emailSecondaire: '',
      siteWeb: '',
      adresse: '',
      codePostal: 0,
      ville: '',
      pays: '',
    };
    this.errorMessage = '';
  }   
  
  toggleEditFormContact(contact?: Contact) {
    if (contact) {
      this.selectedContact = { ...contact };
      this.showEditFormContact = true;
    } else {
      this.showEditFormContact = false;
    }
  }  

  toggleContactSidePanel(client: Client): void {
    this.selectedClientForContacts = client;
    this.loadContacts(client.id!); 
    this.showContactSidePanel = true;
  }    

  addContact() {
    if (!this.newContact.nom || !this.newContact.prenom || !this.newContact.fonction ||
        !this.newContact.fax || !this.newContact.mobile || !this.newContact.telephone || !this.newContact.email) {
      this.errorMessage = 'Veuillez remplir tous les champs obligatoires.';
      return;
    }
  
    const nameRegex = /^[a-zA-ZéèêàâîïôùçÉÈÊÀÂÎÏÔÙÇ\-\'\s]+$/;
    if (!nameRegex.test(this.newContact.nom)) {
      this.errorMessage = 'Le nom ne doit contenir que des lettres.';
      return;
    }
  
    const prenameRegex = /^[a-zA-ZéèêàâîïôùçÉÈÊÀÂÎÏÔÙÇ\-\'\s]+$/;
    if (!prenameRegex.test(this.newContact.prenom)) {
      this.errorMessage = 'Le prénom ne doit contenir que des lettres.';
      return;
    }
  
    const mobileStr = this.newContact.mobile?.toString().trim();
    if (!/^\d{8}$/.test(mobileStr)) {
      this.errorMessage = 'Le numéro de mobile doit contenir exactement 8 chiffres.';
      return;
    }
  
    if (this.newContact.telephone) {
      const telStr = this.newContact.telephone.toString().trim();
      if (!/^\d{8}$/.test(telStr)) {
        this.errorMessage = 'Le numéro de téléphone doit contenir exactement 8 chiffres.';
        return;
      }
    }
  
    const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    if (!emailRegex.test(this.newContact.email)) {
      this.errorMessage = 'Adresse email est invalide.';
      return;
    }
  
    const cpStr = this.newContact.codePostal!.toString().trim();
    if (!/^\d{4}$/.test(cpStr)) {
      this.errorMessage = 'Le code postal doit contenir 4 chiffres.';
      return;
    }
  
    this.newContact.client = { id: this.selectedClient!.id };
  
    this.contactService.addContact(this.newContact).subscribe({
      next: () => {
        this.toggleAddFormContact();
        this.loadContacts(this.selectedClient!.id);
        this.errorMessage = '';
      },
      error: err => {
        this.errorMessage = 'Erreur lors de l\'ajout du contact.';
      }
    });
  }        
  
  updateContact() {
    if (!this.selectedContact) return;
  
    this.selectedContact.clientId = this.clientId;
  
    this.contactService.updateContact(this.selectedContact.id!, this.selectedContact).subscribe({
      next: () => {
        const index = this.contacts.findIndex(c => c.id === this.selectedContact!.id);
        if (index !== -1) {
          this.contacts[index] = { ...this.selectedContact! };
        }
  
        this.toggleEditFormContact();
  
        this.successMessage = 'Contact mis à jour avec succès !';
        setTimeout(() => this.successMessage = '', 3000);
      },
      error: err => {
        console.error(err);
        this.errorMessage = 'Erreur lors de la mise à jour du contact.';
        setTimeout(() => this.errorMessage = '', 3000);
      }
    });
  }    

  deleteSelectedContact(contact: Contact): void {
    if (contact?.id) {
      this.contactService.deleteContact(contact.id).subscribe({
        next: () => {
          this.contacts = this.contacts.filter(c => c.id !== contact.id);
          alert('Contact supprimé avec succès');
          this.loadContacts(this.clientId);
        },
        error: (err) => {
          alert('Erreur lors de la suppression du contact');
        }
      });
    }
  }  
  
  updateClient(): void {
    if (this.selectedClient) {
      this.clientService.updateClient(this.selectedClient).subscribe({
        next: (updatedClient) => {
  
          const index = this.clients.findIndex(client => client.id === updatedClient.id);
          if (index !== -1) {
            this.clients[index] = updatedClient;
            this.filteredClients = [...this.clients]; 
          }
          this.successMessage = 'Client mis à jour avec succès !';
          this.toggleEditForm(); 
        }
      });
    }
  }

  toggleActions(client: any): void {
    this.openedClient = this.openedClient === client ? null : client;
  }

  selectClientToEdit(client: Client): void {
    this.selectedClient = { ...client };
    this.showEditForm = true; 
  }
  
  deleteClient(id: number): void {
    if (confirm("Êtes-vous sûr de vouloir supprimer ce client ?")) {
      this.clientService.deleteClient(id).subscribe({
        next: () => {
          this.loadClients();
          this.successMessage = 'Client supprimé avec succès !'; 
        }
      });
    }
  }  
}
