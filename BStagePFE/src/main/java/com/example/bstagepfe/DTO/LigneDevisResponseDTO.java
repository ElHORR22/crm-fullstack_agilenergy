package com.example.bstagepfe.DTO;
import java.math.BigDecimal;

public class LigneDevisResponseDTO {

    private Long id;
    private String codeProduit;
    private String libelleProduit;
    private String codeEmballage;

    private BigDecimal quantite;
    private BigDecimal prixUnitaireHT;

    private BigDecimal ecoZit;

    private BigDecimal prixTTC;
    private BigDecimal poidsLigneKg;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCodeProduit() { return codeProduit; }
    public void setCodeProduit(String codeProduit) { this.codeProduit = codeProduit; }

    public String getLibelleProduit() { return libelleProduit; }
    public void setLibelleProduit(String libelleProduit) { this.libelleProduit = libelleProduit; }

    public String getCodeEmballage() { return codeEmballage; }
    public void setCodeEmballage(String codeEmballage) { this.codeEmballage = codeEmballage; }

    public BigDecimal getQuantite() { return quantite; }
    public void setQuantite(BigDecimal quantite) { this.quantite = quantite; }

    public BigDecimal getPrixUnitaireHT() { return prixUnitaireHT; }
    public void setPrixUnitaireHT(BigDecimal prixUnitaireHT) { this.prixUnitaireHT = prixUnitaireHT; }

    public BigDecimal getEcoZit() { return ecoZit; }
    public void setEcoZit(BigDecimal ecoZit) { this.ecoZit = ecoZit; }

    public BigDecimal getPrixTTC() { return prixTTC; }
    public void setPrixTTC(BigDecimal prixTTC) { this.prixTTC = prixTTC; }

    public BigDecimal getPoidsLigneKg() { return poidsLigneKg; }
    public void setPoidsLigneKg(BigDecimal poidsLigneKg) { this.poidsLigneKg = poidsLigneKg; }
}