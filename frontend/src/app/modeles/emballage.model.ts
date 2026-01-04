export class Emballage {
    id!: number;
    codeEmballage!: number;
    libelle: string ='';
    poidsEmballage!: number;
    typemballage: string ='';
    dateCreation?: Date;
    dateModification?: Date;
    isDeleted!: boolean;
    showDropdown?: boolean;
    checked?: boolean;
}