package com.example.bstagepfe.controllers;

import com.example.bstagepfe.DTO.LoginRequest;
import com.example.bstagepfe.entities.Utilisateur;
import com.example.bstagepfe.repos.UtilisateurRepository;
import com.example.bstagepfe.services.AuthService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HexFormat;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    private static final int MDP_LONGUEUR_MIN = 8;

    private final AuthService authService;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${app.frontend-url:http://localhost:4200}")
    private String frontendUrl;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @GetMapping("/me")
    public Map<String, Object> me(Principal principal) {
        return Map.of("email", principal.getName());
    }

    // Réponse identique que l'email existe ou non (et même si l'envoi échoue),
    // pour ne pas permettre de deviner quels comptes existent.
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        Optional<Utilisateur> userOpt = email == null ? Optional.empty() : utilisateurRepository.findByEmail(email);

        if (userOpt.isPresent()) {
            Utilisateur user = userOpt.get();

            String token = UUID.randomUUID().toString();
            user.setResetToken(sha256(token));
            user.setTokenExpiration(LocalDateTime.now().plusMinutes(15));
            utilisateurRepository.save(user);

            try {
                sendResetEmail(user.getEmail(), token);
            } catch (MailException e) {
                log.error("Échec de l'envoi du mail de réinitialisation : {}", e.getMessage());
            }
        }

        return ResponseEntity.ok().body(Map.of("message", "Si cet email existe, un lien a été envoyé."));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        String password = request.get("password");

        if (password == null || password.length() < MDP_LONGUEUR_MIN) {
            return ResponseEntity.badRequest().body(Map.of("message",
                    "Le mot de passe doit contenir au moins " + MDP_LONGUEUR_MIN + " caractères."));
        }

        Optional<Utilisateur> userOpt = token == null || token.isBlank()
                ? Optional.empty()
                : utilisateurRepository.findByResetToken(sha256(token));

        if (userOpt.isEmpty()
                || userOpt.get().getTokenExpiration() == null
                || userOpt.get().getTokenExpiration().isBefore(LocalDateTime.now())) {
            return ResponseEntity.badRequest().body(Map.of("message", "Lien invalide ou expiré."));
        }

        Utilisateur user = userOpt.get();
        user.setMdp(passwordEncoder.encode(password));
        user.setResetToken(null);
        user.setTokenExpiration(null);
        utilisateurRepository.save(user);

        return ResponseEntity.ok(Map.of("message", "Mot de passe réinitialisé."));
    }

    private void sendResetEmail(String email, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Réinitialisation de votre mot de passe - AgilEnergy");
        message.setText("Cliquez ici pour changer votre mot de passe (lien valable 15 minutes) : " +
                frontendUrl + "/reset-password?token=" + token);
        mailSender.send(message);
    }

    private static String sha256(String valeur) {
        try {
            byte[] hash = MessageDigest.getInstance("SHA-256").digest(valeur.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }
}
