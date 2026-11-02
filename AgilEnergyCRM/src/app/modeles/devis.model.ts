import { Client } from "./client.model";
import { LigneDevis } from "./lignedevis.model";
import { Prospect } from "./prospect.model";

// Cycle de vie : BROUILLON -> ENVOYE -> ACCEPTE ou REFUSE (même règle que StatutDevis côté Java).
export type StatutDevis = 'BROUILLON' | 'ENVOYE' | 'ACCEPTE' | 'REFUSE';

export const STATUTS_DEVIS: StatutDevis[] = ['BROUILLON', 'ENVOYE', 'ACCEPTE', 'REFUSE'];

export const LIBELLES_STATUT: Record<StatutDevis, string> = {
    BROUILLON: 'Brouillon',
    ENVOYE: 'Envoyé',
    ACCEPTE: 'Accepté',
    REFUSE: 'Refusé'
};

// Libellé du bouton qui fait passer un devis au statut donné.
export const ACTIONS_STATUT: Record<StatutDevis, string> = {
    BROUILLON: 'Repasser en brouillon',
    ENVOYE: 'Marquer comme envoyé',
    ACCEPTE: 'Marquer comme accepté',
    REFUSE: 'Marquer comme refusé'
};

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
    statut?: StatutDevis;
    dateStatut?: string;
    // Statuts atteignables depuis le statut actuel (calculés par le backend).
    transitionsPossibles?: StatutDevis[];
    // Envoyé mais échéance dépassée sans réponse.
    expire?: boolean;
    ligneDevis?: LigneDevis[];
    showDropdown?: boolean;
    checked?: boolean;
} 