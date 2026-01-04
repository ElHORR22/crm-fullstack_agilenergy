package com.example.bstagepfe.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class TVA {

    @Id
    private Long id = 1L;

    private int valeur;

    public TVA() {}

    public TVA(int valeur) {
        this.valeur = valeur;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getValeur() {
        return valeur;
    }

    public void setValeur(int valeur) {
        this.valeur = valeur;
    }
}

