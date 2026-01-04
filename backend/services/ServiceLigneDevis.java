package com.example.bstagepfe.services;
import com.example.bstagepfe.entities.LigneDevis;
import com.example.bstagepfe.repos.LigneDevisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ServiceLigneDevis {

    @Autowired
    private LigneDevisRepository ligneDevisRepository;

    public LigneDevis addLigneDevis(LigneDevis ligneDevis) {
        return ligneDevisRepository.save(ligneDevis);
    }

    public List<LigneDevis> getAllLignesDevis() {
        return ligneDevisRepository.findAll();
    }

    public Optional<LigneDevis> getLigneDevisById(Long id) {
        return ligneDevisRepository.findById(id);
    }

    public List<LigneDevis> getLignesByDevisId(Long devisId) {
        return ligneDevisRepository.findByDevisId(devisId);
    }

    public LigneDevis updateLigneDevis(Long id, LigneDevis detailsLigneDevis) {
        return ligneDevisRepository.findById(id).map(ligneDevis -> {
            ligneDevis.setCodeProduit(detailsLigneDevis.getCodeProduit());
            ligneDevis.setLibelleProduit(detailsLigneDevis.getLibelleProduit());
            ligneDevis.setCodeEmballage(detailsLigneDevis.getCodeEmballage());
            ligneDevis.setQuantite(detailsLigneDevis.getQuantite());
            ligneDevis.setPrixUnitaireHT(detailsLigneDevis.getPrixUnitaireHT());
            ligneDevis.setTva(detailsLigneDevis.getTva());
            ligneDevis.setEcoZit(detailsLigneDevis.getEcoZit());
            ligneDevis.setPrixTTC(detailsLigneDevis.getPrixTTC());
            ligneDevis.setPoidsLigneKg(detailsLigneDevis.getPoidsLigneKg());
            return ligneDevisRepository.save(ligneDevis);
        }).orElse(null);
    }

    public void deleteLigneDevis(Long id) {
        ligneDevisRepository.deleteById(id);
    }
}
