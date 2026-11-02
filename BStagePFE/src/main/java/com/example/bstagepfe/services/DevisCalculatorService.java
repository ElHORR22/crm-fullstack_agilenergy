package com.example.bstagepfe.services;

import com.example.bstagepfe.entities.Client;
import com.example.bstagepfe.entities.Devis;
import com.example.bstagepfe.entities.LigneDevis;
import com.example.bstagepfe.repos.EmballageRepository;
import com.example.bstagepfe.repos.ProduitRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Service
public class DevisCalculatorService {

    private final ProduitRepository produitRepository;
    private final EmballageRepository emballageRepository;

    public DevisCalculatorService(ProduitRepository produitRepository,
                                  EmballageRepository emballageRepository) {
        this.produitRepository = produitRepository;
        this.emballageRepository = emballageRepository;
    }

    public void compute(Devis devis) {
        if (devis.getLigneDevis() == null || devis.getLigneDevis().isEmpty()) {
            devis.setTotalTTC(BigDecimal.ZERO);
            devis.setTotalPoidsKg(BigDecimal.ZERO);
            return;
        }

        BigDecimal totalTtc = BigDecimal.ZERO;
        BigDecimal totalPoids = BigDecimal.ZERO;
        boolean exonere = estExonere(devis.getClient());

        for (LigneDevis l : devis.getLigneDevis()) {

            // 1) attacher le devis à la ligne (important pour cascade + FK)
            l.setDevis(devis);

            // 2) récupérer quantite, prixHT
            BigDecimal qte = nz(l.getQuantite());
            BigDecimal puHt = nz(l.getPrixUnitaireHT());

            // 3) TVA (chez toi c'est int dans TVA -> pas de null check sur int)
            int tva = (!exonere && l.getTva() != null) ? l.getTva().getValeur() : 0;
            BigDecimal tauxTva = BigDecimal.valueOf(tva).divide(BigDecimal.valueOf(100));

            // 4) récupérer poids produit depuis codeProduit
            BigDecimal poidsProduit = getPoidsProduitByCode(l.getCodeProduit());

            // 5) récupérer poids emballage depuis codeEmballage (conversion String -> Integer)
            BigDecimal poidsEmballage = getPoidsEmballageByCode(l.getCodeEmballage());

            // 6) ecoZit + poids ligne (même logique que ton frontend)
            BigDecimal poidsDiff = poidsProduit.subtract(poidsEmballage);
            BigDecimal ecoZit = poidsDiff.max(BigDecimal.ZERO)
                    .multiply(BigDecimal.valueOf(0.06))
                    .multiply(qte)
                    .setScale(3, RoundingMode.HALF_UP);

            BigDecimal poidsLigne = poidsProduit.add(poidsEmballage)
                    .multiply(qte)
                    .setScale(3, RoundingMode.HALF_UP);

            // 7) TTC
            BigDecimal ttc = puHt.multiply(qte)
                    .multiply(BigDecimal.ONE.add(tauxTva))
                    .add(ecoZit)
                    .setScale(3, RoundingMode.HALF_UP);

            // 8) set dans la ligne
            l.setEcoZit(ecoZit);
            l.setPoidsLigneKg(poidsLigne);
            l.setPrixTTC(ttc);

            totalTtc = totalTtc.add(ttc);
            totalPoids = totalPoids.add(poidsLigne);
        }

        devis.setTotalTTC(totalTtc.setScale(3, RoundingMode.HALF_UP));
        devis.setTotalPoidsKg(totalPoids.setScale(3, RoundingMode.HALF_UP));
    }

    private BigDecimal getPoidsProduitByCode(String codeProduit) {
        if (codeProduit == null || codeProduit.isBlank()) return BigDecimal.ZERO;

        return produitRepository.findFirstByCodeProduitAndIsDeletedFalse(codeProduit)
                .map(p -> BigDecimal.valueOf(p.getPoidsProduit()))
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Produit introuvable: " + codeProduit
                ));
    }

    private BigDecimal getPoidsEmballageByCode(String codeEmballage) {
        if (codeEmballage == null || codeEmballage.isBlank()) return BigDecimal.ZERO;

        Integer code = toInteger(codeEmballage, "codeEmballage invalide: " + codeEmballage);

        return emballageRepository.findFirstByCodeEmballageAndIsDeletedFalse(code)
                .map(e -> BigDecimal.valueOf(e.getPoidsEmballage() == null ? 0.0 : e.getPoidsEmballage()))
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Emballage introuvable: " + codeEmballage
                ));
    }

    // Client exonéré de TVA : flag actif et date limite non dépassée (pas de date = sans limite).
    static boolean estExonere(Client client) {
        if (client == null || !Boolean.TRUE.equals(client.getExonere())) return false;
        LocalDate limite = client.getDateLimiteExo();
        return limite == null || !limite.isBefore(LocalDate.now());
    }

    private Integer toInteger(String v, String msg) {
        try { return Integer.valueOf(v); }
        catch (Exception ex) { throw new ResponseStatusException(HttpStatus.BAD_REQUEST, msg); }
    }

    private BigDecimal nz(BigDecimal v) {
        return v == null ? BigDecimal.ZERO : v;
    }
}