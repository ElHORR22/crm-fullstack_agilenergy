package com.example.bstagepfe.controllers;
import com.example.bstagepfe.DTO.MoisDevis;
import com.example.bstagepfe.DTO.ProduitDemandes;
import com.example.bstagepfe.repos.ClientRepository;
import com.example.bstagepfe.repos.ProspectRepository;
import com.example.bstagepfe.repos.DevisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/dashboard")
@PreAuthorize("hasRole('ADMINISTRATEUR')")
public class DashboardController {

    @Autowired
    private DevisRepository devisRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ProspectRepository prospectRepository;

    @GetMapping("/chiffre-affaires")
    public Double getChiffreAffairesTotal() {
        return devisRepository.getChiffreAffairesTotal();
    }

    @GetMapping("/clients-par-gouvernorat")
    public List<Object[]> getClientsParGouvernorat() {
        return clientRepository.countClientsByGouvernorat();
    }

    @GetMapping("/clients-par-secteur")
    public List<Object[]> getClientsParSecteur() {
        return clientRepository.countClientsBySecteurActivite();
    }

    @GetMapping("/ca-mensuel")
    public Double getCaMensuel(@RequestParam int annee, @RequestParam int mois) {
        return devisRepository.getChiffreAffairesParMois(annee, mois);
    }

    @GetMapping("/devis-par-mois")
    public List<MoisDevis> getDevisParMois() {
        List<Object[]> rawData = devisRepository.findDevisCountByMonthLastYearNative();
        return rawData.stream()
                .map(obj -> new MoisDevis((String) obj[0], ((Number) obj[1]).longValue()))
                .collect(Collectors.toList());
    }

    @GetMapping("/produits-top5")
    public List<ProduitDemandes> getTopProduits() {
        List<ProduitDemandes> allProduits = devisRepository.findTopProduitsDemandes();
        return allProduits.stream()
                .limit(5)
                .collect(Collectors.toList());
    }

    @GetMapping("/clients-prospects-count")
    public Map<String, Long> getClientsProspectsCount() {
        long clientsCount = clientRepository.countByIsDeletedFalse();
        long prospectsCount = prospectRepository.countByIsDeletedFalse();

        Map<String, Long> result = new HashMap<>();
        result.put("clients", clientsCount);
        result.put("prospects", prospectsCount);

        return result;
    }

}