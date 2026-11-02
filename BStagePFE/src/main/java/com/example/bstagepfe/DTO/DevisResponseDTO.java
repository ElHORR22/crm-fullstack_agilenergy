package com.example.bstagepfe.DTO;

import com.example.bstagepfe.entities.StatutDevis;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public class DevisResponseDTO {

    private Long id;
    private String sujetDevis;

    private LocalDate echeance;
    private String delaiLivraison;
    private String modeLivraison;
    private String modePaiement;

    private LocalDate dateCreation;
    private LocalDate dateModification;

    private BigDecimal totalTTC;
    private BigDecimal totalPoidsKg;

    private Long clientId;
    private Long prospectId;

    private String nomClient;
    private String nomProspect;

    private StatutDevis statut;
    private LocalDate dateStatut;
    // Statuts atteignables depuis le statut actuel : le front s'en sert pour proposer les actions.
    private Set<StatutDevis> transitionsPossibles;
    // Devis envoyé dont l'échéance est passée sans réponse.
    private boolean expire;

    private List<LigneDevisResponseDTO> ligneDevis;

    public StatutDevis getStatut() { return statut; }
    public void setStatut(StatutDevis statut) { this.statut = statut; }

    public LocalDate getDateStatut() { return dateStatut; }
    public void setDateStatut(LocalDate dateStatut) { this.dateStatut = dateStatut; }

    public Set<StatutDevis> getTransitionsPossibles() { return transitionsPossibles; }
    public void setTransitionsPossibles(Set<StatutDevis> transitionsPossibles) { this.transitionsPossibles = transitionsPossibles; }

    public boolean isExpire() { return expire; }
    public void setExpire(boolean expire) { this.expire = expire; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSujetDevis() { return sujetDevis; }
    public void setSujetDevis(String sujetDevis) { this.sujetDevis = sujetDevis; }

    public LocalDate getEcheance() { return echeance; }
    public void setEcheance(LocalDate echeance) { this.echeance = echeance; }

    public String getDelaiLivraison() { return delaiLivraison; }
    public void setDelaiLivraison(String delaiLivraison) { this.delaiLivraison = delaiLivraison; }

    public String getModeLivraison() { return modeLivraison; }
    public void setModeLivraison(String modeLivraison) { this.modeLivraison = modeLivraison; }

    public String getModePaiement() { return modePaiement; }
    public void setModePaiement(String modePaiement) { this.modePaiement = modePaiement; }

    public LocalDate getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDate dateCreation) { this.dateCreation = dateCreation; }

    public LocalDate getDateModification() { return dateModification; }
    public void setDateModification(LocalDate dateModification) { this.dateModification = dateModification; }

    public BigDecimal getTotalTTC() { return totalTTC; }
    public void setTotalTTC(BigDecimal totalTTC) { this.totalTTC = totalTTC; }

    public BigDecimal getTotalPoidsKg() { return totalPoidsKg; }
    public void setTotalPoidsKg(BigDecimal totalPoidsKg) { this.totalPoidsKg = totalPoidsKg; }

    public Long getClientId() { return clientId; }
    public void setClientId(Long clientId) { this.clientId = clientId; }

    public Long getProspectId() { return prospectId; }
    public void setProspectId(Long prospectId) { this.prospectId = prospectId; }

    public String getNomClient() { return nomClient; }
    public void setNomClient(String nomClient) { this.nomClient = nomClient; }

    public String getNomProspect() { return nomProspect; }
    public void setNomProspect(String nomProspect) { this.nomProspect = nomProspect; }

    public List<LigneDevisResponseDTO> getLigneDevis() { return ligneDevis; }
    public void setLigneDevis(List<LigneDevisResponseDTO> ligneDevis) { this.ligneDevis = ligneDevis; }
}

