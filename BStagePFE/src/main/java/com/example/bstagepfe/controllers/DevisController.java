package com.example.bstagepfe.controllers;

import com.example.bstagepfe.DTO.DevisRequestDTO;
import com.example.bstagepfe.DTO.DevisResponseDTO;
import com.example.bstagepfe.services.ServiceDevis;
import com.example.bstagepfe.services.ServiceParametrage;
import com.itextpdf.text.DocumentException;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/devis")
public class DevisController {

    private final ServiceDevis serviceDevis;
    private final ServiceParametrage serviceParametrage;

    @Autowired
    public DevisController(ServiceDevis serviceDevis, ServiceParametrage serviceParametrage) {
        this.serviceDevis = serviceDevis;
        this.serviceParametrage = serviceParametrage;
    }

    @GetMapping("/tva")
    public ResponseEntity<Integer> getTvaPourDevis() {
        return ResponseEntity.ok(serviceParametrage.getTVA().getValeur());
    }

    @PostMapping
    public ResponseEntity<DevisResponseDTO> addDevisFromDto(@Valid @RequestBody DevisRequestDTO dto) {
        DevisResponseDTO saved = serviceDevis.addDevisFromDto(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping
    public ResponseEntity<List<DevisResponseDTO>> getAllDevis() {
        return ResponseEntity.ok(serviceDevis.getAllDevisDto());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DevisResponseDTO> getDevisById(@PathVariable Long id) {
        return ResponseEntity.ok(serviceDevis.getDevisDtoById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DevisResponseDTO> updateDevisFromDto(@PathVariable Long id,
                                                               @Valid @RequestBody DevisRequestDTO dto) {
        return ResponseEntity.ok(serviceDevis.updateDevisFromDto(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDevis(@PathVariable Long id) {
        serviceDevis.deleteDevis(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/par-email")
    public ResponseEntity<List<DevisResponseDTO>> getDevisByEmail(@RequestParam String email) {
        return ResponseEntity.ok(serviceDevis.getDevisByEmailDto(email));
    }

    @GetMapping("/mes-devis")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<List<DevisResponseDTO>> getMesDevis(Principal principal) {
        String email = principal.getName();
        return ResponseEntity.ok(serviceDevis.getDevisByEmailDto(email));
    }

    @GetMapping("/{id}/pdf")
    public void exportPDF(@PathVariable Long id, HttpServletResponse response) throws IOException, DocumentException {
        serviceDevis.generatePDF(id, response);
    }
}