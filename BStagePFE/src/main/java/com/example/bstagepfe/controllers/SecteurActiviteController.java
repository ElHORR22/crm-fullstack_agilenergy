package com.example.bstagepfe.controllers;
import com.example.bstagepfe.services.ServiceSecteurActivite;
import com.example.bstagepfe.entities.SecteurActivite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/secteurs")
public class SecteurActiviteController {

    @Autowired
    private ServiceSecteurActivite ServicesecteurActivite;

    @GetMapping
    public List<SecteurActivite> getAllSecteurs() {
        return ServicesecteurActivite.getAllSecteurs();
    }

    @GetMapping("/nom/{nom}")
    public Optional<SecteurActivite> getSecteurByNom(@PathVariable String nom) {
        return ServicesecteurActivite.getSecteurByNom(nom);
    }

    @DeleteMapping("/nom/{nom}")
    public void deleteSecteurByNom(@PathVariable String nom) {
        ServicesecteurActivite.deleteSecteurByNom(nom);
    }
}

