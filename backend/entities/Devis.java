package com.example.bstagepfe.entities;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
public class Devis {

    @Id
    @GeneratedValue
    private Long id;

    private String sujetDevis;
    private String nomClient;
    private String nomProspect;

    private LocalDate echeance;
    private String delaiLivraison;
    private String modeLivraison;
    private String modePaiement;

    private LocalDate dateCreation;
    private LocalDate dateModification;

    private BigDecimal totalTTC;
    private BigDecimal totalPoidsKg;

    @OneToMany(mappedBy = "devis", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JsonManagedReference
    private List<LigneDevis> ligneDevis;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSujetDevis() {
        return sujetDevis;
    }

    public void setSujetDevis(String sujetDevis) {
        this.sujetDevis = sujetDevis;
    }

    public String getNomClient() {
        return nomClient;
    }

    public void setNomClient(String nomClient) {
        this.nomClient = nomClient;
    }

    public String getNomProspect() {
        return nomProspect;
    }

    public void setNomProspect(String nomProspect) {
        this.nomProspect = nomProspect;
    }

    public LocalDate getEcheance() {
        return echeance;
    }

    public void setEcheance(LocalDate echeance) {
        this.echeance = echeance;
    }

    public String getDelaiLivraison() {
        return delaiLivraison;
    }

    public void setDelaiLivraison(String delaiLivraison) {
        this.delaiLivraison = delaiLivraison;
    }

    public String getModeLivraison() {
        return modeLivraison;
    }

    public void setModeLivraison(String modeLivraison) {
        this.modeLivraison = modeLivraison;
    }

    public String getModePaiement() {
        return modePaiement;
    }

    public void setModePaiement(String modePaiement) {
        this.modePaiement = modePaiement;
    }

    public LocalDate getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }

    public LocalDate getDateModification() {
        return dateModification;
    }

    public void setDateModification(LocalDate dateModification) {
        this.dateModification = dateModification;
    }

    public BigDecimal getTotalTTC() {
        return totalTTC;
    }

    public void setTotalTTC(BigDecimal totalTTC) {
        this.totalTTC = totalTTC;
    }

    public BigDecimal getTotalPoidsKg() {
        return totalPoidsKg;
    }

    public void setTotalPoidsKg(BigDecimal totalPoidsKg) {
        this.totalPoidsKg = totalPoidsKg;
    }

    public List<LigneDevis> getLigneDevis() {
        return ligneDevis;
    }

    public void setLigneDevis(List<LigneDevis> ligneDevis) {
        this.ligneDevis = ligneDevis;
    }
}
