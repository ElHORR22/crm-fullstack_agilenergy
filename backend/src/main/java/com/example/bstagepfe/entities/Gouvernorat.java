package com.example.bstagepfe.entities;
import jakarta.persistence.*;

@Entity
public class Gouvernorat {

    @Id
    private Long id;

    @Column(unique = true, nullable = false)
    private String nom;

    public Gouvernorat() {}

    public Gouvernorat(Long id, String nom) {
        this.id = id;
        this.nom = nom;
    }

    public Long getId() { return id; }

    public String getNom() { return nom; }

    public void setNom(String nomG) { this.nom = nom; }
}


