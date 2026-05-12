import { Tva } from "./parametrage.model";

export interface LigneDevis {
    id?: number;
    codeProduit: string;
    libelleProduit: string;
    codeEmballage: number;
    quantite: number;
    prixUnitaireHT: number;
    tva: Tva;
    ecoZit: number;
    prixTTC: number;
    poidsLigneKg: number;
}