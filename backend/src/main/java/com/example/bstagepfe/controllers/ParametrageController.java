package com.example.bstagepfe.controllers;
import com.example.bstagepfe.entities.SecteurActivite;
import com.example.bstagepfe.entities.SourceProspection;
import com.example.bstagepfe.entities.TVA;
import com.example.bstagepfe.services.ServiceParametrage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/parametrage")
public class ParametrageController {

    private final ServiceParametrage serviceParametrage;

    @Autowired
    public ParametrageController(ServiceParametrage serviceParametrage) {
        this.serviceParametrage = serviceParametrage;
    }

    @GetMapping("/tva")
    public ResponseEntity<TVA> getTVA() {
        return ResponseEntity.ok(serviceParametrage.getTVA());
    }

    @PutMapping("/tva")
    public ResponseEntity<TVA> updateTva(@RequestBody TVA tva) {
        TVA updatedTva = serviceParametrage.updateTVA(tva);
        return ResponseEntity.ok(updatedTva);
    }

    @GetMapping("/sources")
    public List<SourceProspection> getSources() {
        return serviceParametrage.getAllSources();
    }

    @PostMapping("/sources")
    public SourceProspection addSource(@RequestBody SourceProspection s) {
        return serviceParametrage.addSource(s);
    }

    @DeleteMapping("/sources/{id}")
    public void deleteSource(@PathVariable Long id) {
        serviceParametrage.deleteSource(id);
    }

    @GetMapping("/secteurs")
    public List<SecteurActivite> getSecteurs() {
        return serviceParametrage.getAllSecteurs();
    }

    @PostMapping("/secteurs")
    public SecteurActivite addSecteur(@RequestBody SecteurActivite secteur) {
        return serviceParametrage.addSecteur(secteur);
    }

    @DeleteMapping("/secteurs/{id}")
    public void deleteSecteur(@PathVariable Long id) {
        serviceParametrage.deleteSecteur(id);
    }
}

