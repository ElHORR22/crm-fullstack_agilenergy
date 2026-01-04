package com.example.bstagepfe.controllers;
import com.example.bstagepfe.entities.LigneDevis;
import com.example.bstagepfe.entities.TVA;
import com.example.bstagepfe.repos.LigneDevisRepository;
import com.example.bstagepfe.services.ServiceLigneDevis;
import com.example.bstagepfe.repos.TVARepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/lignes-devis")
@CrossOrigin(origins = "*")
public class LigneDevisController {

    @Autowired
    private ServiceLigneDevis serviceLigneDevis;

    @Autowired
    private TVARepository tvaRepository;

    @GetMapping
    public List<LigneDevis> getAllLignes() {
        return serviceLigneDevis.getAllLignesDevis();
    }

    @GetMapping("/{id}")
    public ResponseEntity<LigneDevis> getLigneById(@PathVariable Long id) {
        return serviceLigneDevis.getLigneDevisById(id)
                .map(ld -> new ResponseEntity<>(ld, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/by-devis/{devisId}")
    public List<LigneDevis> getLignesByDevis(@PathVariable Long devisId) {
        return serviceLigneDevis.getLignesByDevisId(devisId);
    }

    @PostMapping
    public ResponseEntity<LigneDevis> addLigneDevis(@RequestBody LigneDevis ligneDevis) {
        if (ligneDevis.getTva() == null) {
            throw new RuntimeException("TVA non fournie");
        }

        int valeurTVA = ligneDevis.getTva().getValeur();
        TVA tva = tvaRepository.findByValeur(valeurTVA)
                .orElseThrow(() -> new RuntimeException("TVA avec valeur " + valeurTVA + " non trouvée"));

        ligneDevis.setTva(tva);

        LigneDevis saved = serviceLigneDevis.addLigneDevis(ligneDevis);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LigneDevis> updateLigne(@PathVariable Long id, @RequestBody LigneDevis ligne) {
        LigneDevis updated = serviceLigneDevis.updateLigneDevis(id, ligne);
        if (updated != null) {
            return new ResponseEntity<>(updated, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLigne(@PathVariable Long id) {
        serviceLigneDevis.deleteLigneDevis(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}

