package com.example.bstagepfe.entities;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Produit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;

    private String codeProduit;

    private String libelleProduit;

    private String codeEmballage;

    private String categorie;

    private double poidsProduit;

    private String typeProduit;

    private String actif;
    private String packageProduit;
    private String ecozit;

    private double prixGros;
    private double prixDetail;
    private double prixGerant;

    private LocalDate dateCreation;
    private LocalDate dateModification;

    private boolean IsDeleted;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getCodeProduit() {
        return codeProduit;
    }

    public void setCodeProduit(String codeProduit) {
        this.codeProduit = codeProduit;
    }

    public String getLibelleProduit() {
        return libelleProduit;
    }

    public void setLibelleProduit(String libelleProduit) {
        this.libelleProduit = libelleProduit;
    }

    public String getCodeEmballage() {
        return codeEmballage;
    }

    public void setCodeEmballage(String codeEmballage) {
        this.codeEmballage = codeEmballage;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public double getPoidsProduit() {
        return poidsProduit;
    }

    public void setPoidsProduit(double poidsProduit) {
        this.poidsProduit = poidsProduit;
    }

    public String getTypeProduit() {
        return typeProduit;
    }

    public void setTypeProduit(String typeProduit) {
        this.typeProduit = typeProduit;
    }

    public String getActif() {
        return actif;
    }

    public void setActif(String actif) {
        this.actif = actif;
    }

    public String getPackageProduit() {
        return packageProduit;
    }

    public void setPackageProduit(String packageProduit) {
        this.packageProduit = packageProduit;
    }

    public String getEcozit() {
        return ecozit;
    }

    public void setEcozit(String ecozit) {
        this.ecozit = ecozit;
    }

    public double getPrixGros() {
        return prixGros;
    }

    public void setPrixGros(double prixGros) {
        this.prixGros = prixGros;
    }

    public double getPrixDetail() {
        return prixDetail;
    }

    public void setPrixDetail(double prixDetail) {
        this.prixDetail = prixDetail;
    }

    public double getPrixGerant() {
        return prixGerant;
    }

    public void setPrixGerant(double prixGerant) {
        this.prixGerant = prixGerant;
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

    public boolean getIsDeleted() {
        return IsDeleted;
    }

    public void setIsDeleted(boolean deleted) {
        IsDeleted = deleted;
    }
}
