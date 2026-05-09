package com.example.bstagepfe.DTO;

import com.sun.istack.NotNull;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class LigneDevisRequestDTO {

    @NotBlank private String codeProduit;
    @NotBlank private String libelleProduit;
    @NotBlank private String codeEmballage;

    @NotNull @DecimalMin("0.01")
    private BigDecimal quantite;

    @NotNull @DecimalMin("0.00")
    private BigDecimal prixUnitaireHT;

    @DecimalMin("0.00")
    private BigDecimal ecoZit;

    @DecimalMin("0.00")
    private BigDecimal poidsLigneKg;

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

    public BigDecimal getEcoZit() {
        return ecoZit;
    }

    public void setEcoZit(BigDecimal ecoZit) {
        this.ecoZit = ecoZit;
    }

    public BigDecimal getPoidsLigneKg() {
        return poidsLigneKg;
    }

    public void setPoidsLigneKg(BigDecimal poidsLigneKg) {
        this.poidsLigneKg = poidsLigneKg;
    }
}

