package com.example.bstagepfe.controllers;
import com.example.bstagepfe.services.ServiceSourceProspection;
import com.example.bstagepfe.entities.SourceProspection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/sourcesprospection")
public class SourceProspectionController {

    @Autowired
    private ServiceSourceProspection ServicesourceProspection;

    @GetMapping
    public List<SourceProspection> getAllSources() {
        return ServicesourceProspection.getAllSources();
    }


    @GetMapping("/{nom}")
    public Optional<SourceProspection> getSourceByNom(@PathVariable String nom) {
        return ServicesourceProspection.getSourceByNom(nom);
    }

    @DeleteMapping("/nom/{nom}")
    public void deleteSourceByNom(@PathVariable String nom) {
        ServicesourceProspection.deleteSourceByNom(nom);
    }
}

