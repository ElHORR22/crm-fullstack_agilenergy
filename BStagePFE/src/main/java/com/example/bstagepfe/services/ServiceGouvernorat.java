package com.example.bstagepfe.services;
import com.example.bstagepfe.entities.Gouvernorat;
import com.example.bstagepfe.repos.GouvernoratRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ServiceGouvernorat {

    @Autowired
    private GouvernoratRepository gouvernoratRepository;

    public List<Gouvernorat> getAllGouvernorats() {
        return gouvernoratRepository.findAll();
    }

    public Optional<Gouvernorat> getGouvernoratByNom(String nom) {

        return gouvernoratRepository.findByNom(nom);
    }

    public Optional<Gouvernorat> getGouvernoratById(Long id) {

        return gouvernoratRepository.findById(id);
    }

    @Transactional
    public void deleteGouvernoratByNom(String nom) {
        gouvernoratRepository.deleteByNom(nom);
    }
}


