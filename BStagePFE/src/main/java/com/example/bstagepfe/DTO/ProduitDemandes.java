package com.example.bstagepfe.DTO;

import java.math.BigDecimal;

public class ProduitDemandes {
    private String codeProduit;
    private BigDecimal quantite;

    public ProduitDemandes(String codeProduit, BigDecimal quantite) {
        this.codeProduit = codeProduit;
        this.quantite = quantite;
    }

    public String getCodeProduit() {
        return codeProduit;
    }

    public void setCodeProduit(String codeProduit) {
        this.codeProduit = codeProduit;
    }

    public BigDecimal getQuantite() {
        return quantite;
    }

    public void setQuantite(BigDecimal quantite) {
        this.quantite = quantite;
    }
}


