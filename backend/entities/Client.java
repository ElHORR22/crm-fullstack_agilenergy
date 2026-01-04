package com.example.bstagepfe.entities;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idC;

    @Column(name = "nomC")
    private String nomC;

    private Double numCompte;
    private Double numsousCompte;

    private String matFiscal;
    private Double chiffreA;
    private Integer effectif;

    private Boolean exonere;
    private Boolean isConverted;
    private LocalDate dateLimiteExo;

    private String conditionPaiement;
    private String typeprixAchat;

    private String emailC;
    private String autremail;
    private String siteWeb;

    private int telephoneC;
    private Integer autretelephone;
    private Integer fax;

    private String adresse;
    private int codePostal;
    private String ville;
    private String pays;

    private LocalDate dateInscription;
    private LocalDate dateModification;


    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "secteurActivite_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private SecteurActivite secteurActivite;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "gouvernorat_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Gouvernorat gouvernorat;

    @Column(name = "is_deleted")
    private boolean isDeleted = false;

    @OneToOne
    @JoinColumn(name = "prospect_id")
    private Prospect prospect;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<Contact> contacts;

    public String getNom() {
        return nomC;
    }

    public void setNom(String nomC) {
        this.nomC = nomC;
    }

    public Long getId() {
        return idC;
    }

    public void setId(Long idC) {
        this.idC = idC;
    }

    public Double getNumCompte() {
        return numCompte;
    }

    public void setNumCompte(Double numCompte) {
        this.numCompte = numCompte;
    }

    public Double getNumsousCompte() {
        return numsousCompte;
    }

    public void setNumsousCompte(Double numsousCompte) {
        this.numsousCompte = numsousCompte;
    }

    public String getMatFiscal() {
        return matFiscal;
    }

    public void setMatFiscal(String matFiscal) {
        this.matFiscal = matFiscal;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public Integer getFax() {
        return fax;
    }

    public void setFax(Integer fax) {
        this.fax = fax;
    }

    public Integer getAutretelephone() {
        return autretelephone;
    }

    public void setAutretelephone(Integer autretelephone) {
        this.autretelephone = autretelephone;
    }

    public int getTelephone() {
        return telephoneC;
    }

    public void setTelephone(int telephoneC) {
        this.telephoneC = telephoneC;
    }

    public String getSiteWeb() {
        return siteWeb;
    }

    public void setSiteWeb(String siteWeb) {
        this.siteWeb = siteWeb;
    }

    public String getAutremail() {
        return autremail;
    }

    public void setAutremail(String autremail) {
        this.autremail = autremail;
    }

    public String getEmail() {
        return emailC;
    }

    public void setEmail(String emailC) {
        this.emailC = emailC;
    }

    public Boolean getConverted() {
        return isConverted;
    }

    public void setConverted(Boolean converted) {
        isConverted = converted;
    }

    public LocalDate getDateLimiteExo() {
        return dateLimiteExo;
    }

    public void setDateLimiteExo(LocalDate dateLimiteExo) {
        this.dateLimiteExo = dateLimiteExo;
    }

    public Boolean getExonere() {
        return exonere;
    }

    public void setExonere(Boolean exonere) {
        this.exonere = exonere;
    }

    public Integer getEffectif() {
        return effectif;
    }

    public void setEffectif(Integer effectif) {
        this.effectif = effectif;
    }

    public Double getChiffreA() {
        return chiffreA;
    }

    public void setChiffreA(Double chiffreA) {
        this.chiffreA = chiffreA;
    }

    public int getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(int codePostal) {
        this.codePostal = codePostal;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public LocalDate getDateInscription() {
        return dateInscription;
    }

    public void setDateInscription(LocalDate dateInscription) {
        this.dateInscription = dateInscription;
    }

    public LocalDate getDateModification() {
        return dateModification;
    }

    public void setDateModification(LocalDate dateModification) {
        this.dateModification = dateModification;
    }

    public SecteurActivite getSecteurActivite() {
        return secteurActivite;
    }

    public void setSecteurActivite(SecteurActivite secteurActivite) {
        this.secteurActivite = secteurActivite;
    }

    public Gouvernorat getGouvernorat() {
        return gouvernorat;
    }

    public void setGouvernorat(Gouvernorat gouvernorat) {
        this.gouvernorat = gouvernorat;
    }

    public boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public Prospect getProspect() {
        return prospect;
    }

    public void setProspect(Prospect prospect) {
        this.prospect = prospect;
    }

    public String getConditionPaiement() {
        return conditionPaiement;
    }

    public void setConditionPaiement(String conditionPaiement) {
        this.conditionPaiement = conditionPaiement;
    }

    public String getTypeprixAchat() {
        return typeprixAchat;
    }

    public void setTypeprixAchat(String typeprixAchat) {
        this.typeprixAchat = typeprixAchat;
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }
}
