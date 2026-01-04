package com.example.bstagepfe.entities;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Prospect {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idP;
    private String nomP;
    private String titre;
    private String societe;

    private Double chiffreA;
    private Integer effectif;

    private int mobile;
    private Integer fax;
    private String emailP;
    private int telephoneP;
    private String autremail;

    private String siteWeb;
    private String statutprospect;
    private String adresse;
    private int codePostal;
    private String ville;
    private String pays;

    private LocalDate dateInscription;
    private LocalDate dateModification;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "sourceProspection_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private SourceProspection sourceProspection;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "secteurActivite_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private SecteurActivite secteurActivite;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "gouvernorat_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Gouvernorat gouvernorat;

    @OneToOne(mappedBy = "prospect")
    private Client client;

    public Long getId() {
        return idP;
    }

    public void setId(Long idP) {
        this.idP = idP;
    }

    public String getNom() {
        return nomP;
    }

    public void setNom(String nomP) {
        this.nomP = nomP;
    }

    public String getEmail() {
        return emailP;
    }

    public void setEmail(String emailP) {
        this.emailP = emailP;
    }

    public int getTelephone() {
        return telephoneP;
    }

    public void setTelephone(int telephoneP) {
        this.telephoneP = telephoneP;
    }

    public SourceProspection getSourceProspection() {
        return sourceProspection;
    }

    public void setSourceProspection(SourceProspection sourceProspection) {
        this.sourceProspection = sourceProspection;
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

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public LocalDate getDateModification() {
        return dateModification;
    }

    public void setDateModification(LocalDate dateModification) {
        this.dateModification = dateModification;
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

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public int getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(int codePostal) {
        this.codePostal = codePostal;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getStatutprospect() {
        return statutprospect;
    }

    public void setStatutprospect(String statutprospect) {
        this.statutprospect = statutprospect;
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

    public Integer getFax() {
        return fax;
    }

    public void setFax(Integer fax) {
        this.fax = fax;
    }

    public int getMobile() {
        return mobile;
    }

    public void setMobile(int mobile) {
        this.mobile = mobile;
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

    public String getSociete() {
        return societe;
    }

    public void setSociete(String societe) {
        this.societe = societe;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }
}
