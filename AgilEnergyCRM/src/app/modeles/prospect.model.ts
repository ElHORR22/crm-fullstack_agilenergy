export class Prospect {
    id!: number;
    nom: string ='';
    titre: string ='';
    societe: string ='';

    chiffreA!: number;
    effectif!: number;

    telephone!: number;
    mobile!: number;
    fax!: number;

    email: string ='';
    autremail: string = '';
    siteWeb: string = '';

    statutprospect: string ='';
    adresse: string = '';
    codePostal!: number;
    ville: string = '';
    pays: string = '';

    dateInscription?: Date;
    dateModification?: Date;

    isDeleted!: boolean;
    showDropdown?: boolean;
    checked?: boolean;

    sourceProspection!: SourceProspection;
    secteurActivite!: SecteurActivite;
    gouvernorat!: Gouvernorat;
}

export interface SourceProspection {
    id: number;
    nom?: string;
}

export interface SecteurActivite {
    id: number;
    nom?: string;
}

export interface Gouvernorat {
    id?: number;
    nom: string;
}
