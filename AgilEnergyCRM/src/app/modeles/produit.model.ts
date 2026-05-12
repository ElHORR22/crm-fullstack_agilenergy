export class Produit {
    id!: number;
    codeProduit!: number;
    libelleProduit: string='';
    codeEmballage: string='';

    categorie: string='';
    poidsProduit!: number;
    typeProduit: string='';

    actif: string='';
    packageProduit: string='';
    ecozit: string='';

    prixGros!: number;
    prixDetail!: number;
    prixGerant!: number;

    dateCreation?: Date;
    dateModification?: Date;
    
    isDeleted!: boolean;
    showDropdown?: boolean;
    checked?: boolean;
}