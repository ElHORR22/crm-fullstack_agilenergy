package com.example.bstagepfe.services;

import com.example.bstagepfe.entities.SecteurActivite;
import com.example.bstagepfe.entities.SourceProspection;
import com.example.bstagepfe.entities.TVA;
import com.example.bstagepfe.repos.SecteurActiviteRepository;
import com.example.bstagepfe.repos.SourceProspectionRepository;
import com.example.bstagepfe.repos.TVARepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceParametrage {

    private TVARepository tvaRepository;

    @Autowired
    public ServiceParametrage(TVARepository tvaRepository) {
        this.tvaRepository = tvaRepository;
    }

    @Autowired
    private SourceProspectionRepository sourceRepo;

    @Autowired
    private SecteurActiviteRepository secteurRepo;

    public TVA getTVA() {
        return tvaRepository.findById(1L).orElseGet(() -> {
            TVA tva = new TVA();
            tva.setValeur(19);
            return tvaRepository.save(tva);
        });
    }

    public TVA updateTVA(TVA tva) {
        tva.setValeur(tva.getValeur());
        return tvaRepository.save(tva);
    }

    public List<SourceProspection> getAllSources() {
        return sourceRepo.findAll();
    }

    public SourceProspection addSource(SourceProspection s) {
        return sourceRepo.save(s);
    }

    public void deleteSource(Long id) {
        sourceRepo.deleteById(id);
    }

    public List<SecteurActivite> getAllSecteurs() {
        return secteurRepo.findAll();
    }

    public SecteurActivite addSecteur(SecteurActivite s) {
        return secteurRepo.save(s);
    }

    public void deleteSecteur(Long id) {
        secteurRepo.deleteById(id);
    }
}
