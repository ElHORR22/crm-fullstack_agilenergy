package com.example.bstagepfe.services;
import com.example.bstagepfe.entities.Emballage;
import com.example.bstagepfe.repos.EmballageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ServiceEmballage {

    @Autowired
    private EmballageRepository emballageRepository;

    public List<Emballage> getAllEmballages() {
        return emballageRepository.findAll();
    }

    public Emballage addEmballage(Emballage emballage) {
        return emballageRepository.save(emballage);
    }

    public Emballage updateEmballage(Long id, Emballage detailsemballage) {
        return emballageRepository.findById(id).map(emballage -> {
            emballage.setNom(detailsemballage.getNom());
            emballage.setcodeEmballage(detailsemballage.getcodeEmballage());
            emballage.setLibelle(detailsemballage.getLibelle());
            emballage.setPoidsEmballage(detailsemballage.getPoidsEmballage());
            emballage.setTypemballage(detailsemballage.getTypemballage());
            emballage.setDateCreation(detailsemballage.getDateCreation());
            emballage.setDateModification(detailsemballage.getDateModification());

            return emballageRepository.save(emballage);
        }).orElseThrow(() -> new RuntimeException("Emballage non trouvé !"));
    }

    public void deleteEmballage(Long id) {
        Emballage emballage = emballageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Emballage non trouvé avec l'ID : " + id));
        emballage.setIsDeleted(true);
        emballageRepository.save(emballage);
    }
}
