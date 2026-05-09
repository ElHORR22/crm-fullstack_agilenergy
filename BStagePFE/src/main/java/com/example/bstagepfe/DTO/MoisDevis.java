package com.example.bstagepfe.DTO;

public class MoisDevis {
    private String mois;
    private Long nbDevis;

    public MoisDevis(String mois, Long nbDevis) {
        this.mois = mois;
        this.nbDevis = nbDevis;
    }

    public String getMois() {
        return mois;
    }

    public void setMois(String mois) {
        this.mois = mois;
    }

    public Long getNbDevis() {
        return nbDevis;
    }

    public void setNbDevis(Long nbDevis) {
        this.nbDevis = nbDevis;
    }
}

