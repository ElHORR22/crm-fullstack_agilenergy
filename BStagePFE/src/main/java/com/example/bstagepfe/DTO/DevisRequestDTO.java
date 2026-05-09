package com.example.bstagepfe.DTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;

public class DevisRequestDTO {

    @NotBlank
    private String sujetDevis;

    @NotNull @FutureOrPresent
    private LocalDate echeance;

    @NotBlank
    private String delaiLivraison;

    @NotBlank
    private String modeLivraison;

    @NotBlank
    private String modePaiement;

    private Long clientId;
    private Long prospectId;

    private ClientRefDTO client;
    private ProspectRefDTO prospect;

    @NotEmpty @Valid
    private List<LigneDevisRequestDTO> ligneDevis;

    @AssertTrue(message="Sélectionner soit client soit prospect (pas les deux).")
    public boolean isClientXorProspectValid() {
        return (getClientId() != null) ^ (getProspectId() != null);
    }

    public String getSujetDevis() {
        return sujetDevis;
    }

    public void setSujetDevis(String sujetDevis) {
        this.sujetDevis = sujetDevis;
    }

    public LocalDate getEcheance() {
        return echeance;
    }

    public void setEcheance(LocalDate echeance) {
        this.echeance = echeance;
    }

    public String getDelaiLivraison() {
        return delaiLivraison;
    }

    public void setDelaiLivraison(String delaiLivraison) {
        this.delaiLivraison = delaiLivraison;
    }

    public String getModeLivraison() {
        return modeLivraison;
    }

    public void setModeLivraison(String modeLivraison) {
        this.modeLivraison = modeLivraison;
    }

    public String getModePaiement() {
        return modePaiement;
    }

    public void setModePaiement(String modePaiement) {
        this.modePaiement = modePaiement;
    }

    public Long getClientId() {
        if (clientId != null) return clientId;
        return client != null ? client.getIdC() : null;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Long getProspectId() {
        if (prospectId != null) return prospectId;
        return prospect != null ? prospect.getIdP() : null;
    }

    public void setProspectId(Long prospectId) {
        this.prospectId = prospectId;
    }

    public ClientRefDTO getClient() { return client; }
    public void setClient(ClientRefDTO client) { this.client = client; }

    public ProspectRefDTO getProspect() { return prospect; }
    public void setProspect(ProspectRefDTO prospect) { this.prospect = prospect; }


    public List<LigneDevisRequestDTO> getLigneDevis() {
        return ligneDevis;
    }

    public void setLigneDevis(List<LigneDevisRequestDTO> ligneDevis) {
        this.ligneDevis = ligneDevis;
    }
}

