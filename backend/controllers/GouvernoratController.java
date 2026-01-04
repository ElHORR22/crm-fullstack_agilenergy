package com.example.bstagepfe.controllers;
import com.example.bstagepfe.services.ServiceGouvernorat;
import com.example.bstagepfe.entities.Gouvernorat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/gouvernorats")
public class GouvernoratController {

    @Autowired
    private ServiceGouvernorat gouvernoratService;

    @GetMapping
    public List<Gouvernorat> getAllGouvernorats() {
        return gouvernoratService.getAllGouvernorats();
    }

    @GetMapping("/{nom}")
    public Optional<Gouvernorat> getGouvernoratByNom(@PathVariable String nom) {
        return gouvernoratService.getGouvernoratByNom(nom);
    }

    @GetMapping("/{id}")
    public Optional<Gouvernorat> getGouvernoratById(Long id) {
        return gouvernoratService.getGouvernoratById(id);
    }

    @DeleteMapping("/nom/{nom}")
    public void deleteGouvernoratByNom(@PathVariable String nom) {
        gouvernoratService.deleteGouvernoratByNom(nom);
    }
}

