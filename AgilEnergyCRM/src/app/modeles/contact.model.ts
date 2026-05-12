export interface Contact {
    id?: number;
    nom: string;
    prenom: string;
    fonction: string;
    clientId: number;
    client?: { id: number };
    mobile: number;
    telephone: number;
    fax?: number;
    email: string;
    emailSecondaire?: string;
    siteWeb?: string;
    adresse?: string;
    codePostal?: number;
    ville?: string;
    pays?: string;
    dateCreation?: Date;
    dateModification?: Date;
}
  