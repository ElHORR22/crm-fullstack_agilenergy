package com.example.bstagepfe.controllers;
import com.example.bstagepfe.entities.Devis;
import com.example.bstagepfe.services.ServiceDevis;
import com.example.bstagepfe.services.ServiceParametrage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/devis")
@CrossOrigin(origins = "*")
public class DevisController {

    @Autowired
    private ServiceDevis serviceDevis;

    @Autowired
    private ServiceParametrage serviceParametrage;

    @Autowired
    public DevisController(ServiceParametrage serviceParametrage) {
        this.serviceParametrage = serviceParametrage;
    }

    @GetMapping("/tva")
    public ResponseEntity<Integer> getTvaPourDevis() {
        return ResponseEntity.ok(serviceParametrage.getTVA().getValeur());
    }

    @PostMapping
    public ResponseEntity<Devis> addDevis(@RequestBody Devis devis) {
        Devis saved = serviceDevis.addDevis(devis);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @GetMapping
    public List<Devis> getAllDevis() {
        return serviceDevis.getAllDevis();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Devis> getDevisById(@PathVariable Long id) {
        return serviceDevis.getDevisById(id)
                .map(devis -> new ResponseEntity<>(devis, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Devis> updateDevis(@PathVariable Long id, @RequestBody Devis devis) {
        Devis updated = serviceDevis.updateDevis(id, devis);
        if (updated != null) {
            return new ResponseEntity<>(updated, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDevis(@PathVariable Long id) {
        serviceDevis.deleteDevis(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

