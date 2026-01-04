package com.example.bstagepfe.services;
import com.example.bstagepfe.entities.Devis;
import com.example.bstagepfe.entities.LigneDevis;
import com.example.bstagepfe.repos.DevisRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ServiceDevis {

    @Autowired
    private DevisRepository devisRepository;

    public Devis addDevis(Devis devis) {
        return devisRepository.save(devis);
    }

    public List<Devis> getAllDevis() {
        return devisRepository.findAllWithLignes();
    }

    public Optional<Devis> getDevisById(Long id) {
        return devisRepository.findDevisById(id).map(devis -> {
            devis.getLigneDevis().size();
            return devis;
        });
    }

    @Transactional
    public Devis updateDevis(Long id, Devis devisdetails) {
        return devisRepository.findById(id).map(devis -> {
            devis.setSujetDevis(devisdetails.getSujetDevis());
            devis.setNomClient(devisdetails.getNomClient());
            devis.setNomProspect(devisdetails.getNomProspect());
            devis.setEcheance(devisdetails.getEcheance());
            devis.setDelaiLivraison(devisdetails.getDelaiLivraison());
            devis.setModeLivraison(devisdetails.getModeLivraison());
            devis.setModePaiement(devisdetails.getModePaiement());
            devis.setTotalTTC(devisdetails.getTotalTTC());
            devis.setTotalPoidsKg(devisdetails.getTotalPoidsKg());
            devis.setDateCreation(devisdetails.getDateCreation());
            devis.setDateModification(devisdetails.getDateModification());

            devis.getLigneDevis().clear();

            if (devisdetails.getLigneDevis() != null) {
                for (LigneDevis ligne : devisdetails.getLigneDevis()) {
                    ligne.setDevis(devis);
                    devis.getLigneDevis().add(ligne);
                }
            }

            return devisRepository.save(devis);
        }).orElse(null);
    }

    public void deleteDevis(Long id) {
        devisRepository.deleteById(id);
    }
}

