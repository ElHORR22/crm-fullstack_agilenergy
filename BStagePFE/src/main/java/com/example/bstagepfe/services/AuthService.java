package com.example.bstagepfe.services;
import com.example.bstagepfe.DTO.LoginRequest;
import com.example.bstagepfe.entities.Utilisateur;
import com.example.bstagepfe.repos.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTService jwtService;

    public ResponseEntity<Map<String, Object>> login(LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        String motDePasse = loginRequest.getMdp();

        if (email == null || motDePasse == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Champs manquants"));
        }

        var optionalUser = utilisateurRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(401).body(Map.of("message", "Email ou mot de passe incorrect"));
        }

        Utilisateur user = optionalUser.get();
        if (!passwordEncoder.matches(motDePasse, user.getMdp())) {
            return ResponseEntity.status(401).body(Map.of("message", "Email ou mot de passe incorrect"));
        }
        if (!user.isActif()) {
            return ResponseEntity.status(403).body(Map.of("message", "Compte désactivé"));
        }

        String role = user.getRole().getName().name();
        String token = jwtService.generateToken(user.getEmail(), role);

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("role", role);
        response.put("nom", user.getNom());
        response.put("prenom", user.getPrenom());
        response.put("email", user.getEmail());
        return ResponseEntity.ok(response);
    }

}


