package com.example.bstagepfe.entities;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.time.LocalDate;

@Entity
public class Emballage  {

    @Id
    @GeneratedValue
    private Long id;

    private String nom;
    private Integer codeEmballage;
    private String libelle;

    private double poidsEmballage;
    private String typemballage;

    private LocalDate dateCreation;
    private LocalDate dateModification;

    private boolean isDeleted = false;

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

    public Integer getcodeEmballage() {
        return codeEmballage;
    }

    public void setcodeEmballage(Integer codeEmballage) {
        this.codeEmballage = codeEmballage;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public double getPoidsEmballage() {
        return poidsEmballage;
    }

    public void setPoidsEmballage(double poidsEmballage) {
        this.poidsEmballage = poidsEmballage;
    }

    public String getTypemballage() {
        return typemballage;
    }

    public void setTypemballage(String typemballage) {
        this.typemballage = typemballage;
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
        return isDeleted;
    }

    public void setIsDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
