package com.example.bstagepfe.services;
import com.example.bstagepfe.repos.SourceProspectionRepository;
import com.example.bstagepfe.entities.SourceProspection;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ServiceSourceProspection {

    @Autowired
    private SourceProspectionRepository sourceProspectionRepository;

    public List<SourceProspection> getAllSources() {
        return sourceProspectionRepository.findAll();
    }

    public Optional<SourceProspection> getSourceByNom(String nom) {
        return sourceProspectionRepository.findByNom(nom);
    }

    @Transactional
    public void deleteSourceByNom(String nom) {
        sourceProspectionRepository.deleteByNom(nom);
    }
}
