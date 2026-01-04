package com.example.bstagepfe.services;
import com.example.bstagepfe.entities.SecteurActivite;
import com.example.bstagepfe.repos.SecteurActiviteRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ServiceSecteurActivite {

    @Autowired
    private SecteurActiviteRepository secteurActiviteRepository;

    public List<SecteurActivite> getAllSecteurs() {
        return secteurActiviteRepository.findAll();
    }

    public Optional<SecteurActivite> getSecteurByNom(String nom) {
        return secteurActiviteRepository.findByNom(nom);
    }

    @Transactional
    public void deleteSecteurByNom(String nom) {
        secteurActiviteRepository.deleteByNom(nom);
    }
}

