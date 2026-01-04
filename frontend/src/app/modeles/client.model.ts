
export class Client {
  id!: number;
  nom: string = '';
  numCompte!: number;
  numsousCompte!: number;

  matFiscal: string = '';
  chiffreA!: number;
  effectif!: number;

  exonere!: boolean;
  isConverted!: boolean;
  dateLimiteExo!: string;

  conditionPaiement: string = '';
  typeprixAchat: string = '';

  email: string = '';
  autremail: string = '';
  siteWeb: string = '';

  telephone!: number;
  autretelephone!: number;
  fax!: number;

  adresse: string = '';
  codePostal!: number;
  ville: string = '';
  pays: string = '';

  dateInscription?: Date;
  dateModification?: Date;

  isDeleted!: boolean;

  gouvernorat!: Gouvernorat;
  secteurActivite!: SecteurActivite;

  showDropdown?: boolean;
  showContactsDropdown?: boolean;
  checked?: boolean;
}

export interface SecteurActivite {
  id: number;
  nom?: string;
}

export interface Gouvernorat {
  id?: number;
  nom: string;
}