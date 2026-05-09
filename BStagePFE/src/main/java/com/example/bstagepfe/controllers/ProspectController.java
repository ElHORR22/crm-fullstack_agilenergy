package com.example.bstagepfe.controllers;
import com.example.bstagepfe.entities.Client;
import com.example.bstagepfe.services.ServiceProspect;
import com.example.bstagepfe.entities.SecteurActivite;
import com.example.bstagepfe.entities.Gouvernorat;
import com.example.bstagepfe.entities.Prospect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/prospects")
public class ProspectController {

    @Autowired
    private ServiceProspect Serviceprospect;

    @GetMapping
    public List<Prospect> getAllProspects() {
        return Serviceprospect.getAllProspects();
    }

    @GetMapping("/{idP}")
    public List<Prospect> getProspectById(@PathVariable Long idP) {
        return Serviceprospect.getProspectById(idP);
    }

    @GetMapping("/emailP/{emailP}")
    public List<Prospect> getProspectByEmail(@PathVariable String emailP) {
        return Serviceprospect.getProspectByEmail(emailP);
    }

    @GetMapping("/telephoneP/{telephoneP}")
    public List<Prospect> getProspectByTelephone(@PathVariable int telephoneP) {
        return Serviceprospect.getProspectByTelephone(telephoneP);
    }

    @GetMapping("/nomP/{nomP}")
    public List<Prospect> getProspectsByNom(@PathVariable String nomP) {
        return Serviceprospect.getProspectsByNom(nomP);
    }

    @GetMapping("/secteurActivite/{secteurActivite}")
    public List<Prospect> getProspectBySecteurActivite(@PathVariable SecteurActivite secteurActivite) {
        return Serviceprospect.getProspectBySecteurActivite(secteurActivite);
    }

    @GetMapping("/gouvernorat/{gouvernorat}")
    public List<Prospect> getProspectByGouvernorat(@PathVariable Gouvernorat gouvernorat) {
        return Serviceprospect.getProspectByGouvernorat(gouvernorat);
    }

    @PostMapping
    public Prospect addProspect(@RequestBody Prospect prospect) {
        return Serviceprospect.addProspect(prospect);
    }

    @PutMapping("/{idP}")
    public Prospect updateProspect(@PathVariable Long idP, @RequestBody Prospect prospect) {
        return Serviceprospect.updateProspect(idP, prospect);
    }

    @DeleteMapping("/{idP}")
    public void deleteProspect(@PathVariable Long idP) {
        Serviceprospect.deleteProspect(idP);
    }

    @PostMapping("/convertir")
    public ResponseEntity<Client> convertirEnClient(@RequestBody Prospect prospect) {
        Client nouveauClient = Serviceprospect.convertirEnClient(prospect);
        return ResponseEntity.ok(nouveauClient);
    }

}

