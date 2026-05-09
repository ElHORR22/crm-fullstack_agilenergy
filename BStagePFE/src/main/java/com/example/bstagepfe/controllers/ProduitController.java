package com.example.bstagepfe.controllers;

import com.example.bstagepfe.entities.Emballage;
import com.example.bstagepfe.entities.Produit;
import com.example.bstagepfe.services.ServiceEmballage;
import com.example.bstagepfe.services.ServiceProduit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/produits")
public class ProduitController {

    @Autowired
    private ServiceProduit serviceProduit;

    @GetMapping
    public List<Produit> getAllProduits() {
        return serviceProduit.getAllProduits();
    }

    @PostMapping
    public Produit addProduit(@RequestBody Produit produit) {
        return serviceProduit.addProduit(produit);
    }

    @PutMapping("/{id}")
    public Produit updateProduit(@PathVariable Long id, @RequestBody Produit produit) {
        return serviceProduit.updateProduit(id, produit);
    }

    @DeleteMapping("/{id}")
    public void deleteProduit(@PathVariable Long id) {
        serviceProduit.deleteProduit(id);
    }
}
