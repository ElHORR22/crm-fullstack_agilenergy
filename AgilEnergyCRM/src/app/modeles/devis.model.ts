import { Client } from "./client.model";
import { LigneDevis } from "./lignedevis.model";
import { Prospect } from "./prospect.model";

export class Devis {
    id?: number;
    sujetDevis: string = '';
    client?: Client;
    prospect?: Prospect;
    // Champs renvoyés par l'API (DevisResponseDTO) : le destinataire n'est pas un objet imbriqué.
    clientId?: number | null;
    prospectId?: number | null;
    nomClient?: string;
    nomProspect?: string;
    echeance: string = '';
    delaiLivraison: string = '';
    modeLivraison: string = '';
    modePaiement: string = '';
    dateCreation: string = '';
    dateModification?: string = '';
    totalTTC?: number;
    totalPoidsKg?: number;
    ligneDevis?: LigneDevis[];
    showDropdown?: boolean;
    checked?: boolean;
} 