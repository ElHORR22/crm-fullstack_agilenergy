package com.example.bstagepfe.services;
import com.example.bstagepfe.entities.Utilisateur;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    public ResponseEntity<String> login(Utilisateur utilisateur) {
        String email = utilisateur.getEmail();
        String motDePasse = utilisateur.getMotDePasse();

        if (email == null || motDePasse == null) {
            return ResponseEntity.badRequest().body("Veuillez remplir tous les champs.");
        }

        if (email.equals("admin@agil.com.tn") && motDePasse.equals("admin")) {
            return ResponseEntity.ok("Connexion réussie");
        } else {
            return ResponseEntity.status(401).body("Email ou mot de passe incorrect");
        }
    }
}
