package com.example.bstagepfe.entities;
import jakarta.persistence.*;

@Entity
public class SecteurActivite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String nom;

    public SecteurActivite() {}

    public SecteurActivite(String nom, Long id) {
        this.id = id;
        this.nom = nom;
    }

    public Long getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
}

