export interface Utilisateur {
    id?: number;
    email: string;
    mdp: string;
    nom?: string;
    prenom?: string;
    fonction?: string;
    matricule?: string;
    role?: Role; 
    actif?: boolean;
    mobile?: string;
    telephone?: string;
    adresse?: string;
    ville?: string;
    codePostal?: string;
    dateCreation?: Date;
    dateModification?: Date;
    showDropdown?: boolean;
    checked?: boolean;
}

export interface Role {
    id: number;
    name: string;
}  