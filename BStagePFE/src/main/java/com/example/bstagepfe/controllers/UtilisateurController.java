package com.example.bstagepfe.controllers;
import com.example.bstagepfe.entities.Utilisateur;
import com.example.bstagepfe.repos.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/utilisateurs")
public class UtilisateurController {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public List<Utilisateur> getAllUtilisateurs() {
        return utilisateurRepository.findAll();
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Utilisateur> getUtilisateurByEmail(@PathVariable String email) {
        Optional<Utilisateur> utilisateur = utilisateurRepository.findByEmail(email);
        return utilisateur.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Utilisateur addUtilisateur(@RequestBody Utilisateur utilisateur) {
        String motDePasseBrut = utilisateur.getMdp();
        utilisateur.setMdp(passwordEncoder.encode(motDePasseBrut));
        return utilisateurRepository.save(utilisateur);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Utilisateur> updateUtilisateur(@PathVariable Long id, @RequestBody Utilisateur u) {
        return utilisateurRepository.findById(id).map(existing -> {
            u.setId(id);

            if (u.getMdp() == null || u.getMdp().isBlank()) {
                u.setMdp(existing.getMdp());
            } else {
                u.setMdp(passwordEncoder.encode(u.getMdp()));
            }
            return ResponseEntity.ok(utilisateurRepository.save(u));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public void deleteUtilisateur(@PathVariable Long id) {
        utilisateurRepository.deleteById(id);
    }

}

