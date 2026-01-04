package com.example.bstagepfe.entities;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
public class LigneDevis {

    @Id
    @GeneratedValue
    private Long id;

    private String codeProduit;
    private String libelleProduit;
    private String code;

    private BigDecimal quantite;
    private BigDecimal prixUnitaireHT;

    @ManyToOne
    @JoinColumn(name = "tva_id")
    private TVA tva;

    private BigDecimal ecoZit;
    private BigDecimal prixTTC;
    private BigDecimal poidsLigneKg;

    @ManyToOne
    @JoinColumn(name = "devis_id")
    @JsonBackReference
    private Devis devis;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        return code;
    }

    public void setCodeEmballage(String code) {
        this.code = code;
    }

    public BigDecimal getQuantite() {
        return quantite;
    }

    public void setQuantite(BigDecimal quantite) {
        this.quantite = quantite;
    }

    public BigDecimal getPrixUnitaireHT() {
        return prixUnitaireHT;
    }

    public void setPrixUnitaireHT(BigDecimal prixUnitaireHT) {
        this.prixUnitaireHT = prixUnitaireHT;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public TVA getTva() {
        return tva;
    }

    public void setTva(TVA tva) {
        this.tva = tva;
    }

    public BigDecimal getEcoZit() {
        return ecoZit;
    }

    public void setEcoZit(BigDecimal ecoZit) {
        this.ecoZit = ecoZit;
    }

    public BigDecimal getPrixTTC() {
        return prixTTC;
    }

    public void setPrixTTC(BigDecimal prixTTC) {
        this.prixTTC = prixTTC;
    }

    public BigDecimal getPoidsLigneKg() {
        return poidsLigneKg;
    }

    public void setPoidsLigneKg(BigDecimal poidsLigneKg) {
        this.poidsLigneKg = poidsLigneKg;
    }

    public Devis getDevis() {
        return devis;
    }

    public void setDevis(Devis devis) {
        this.devis = devis;
    }
}
