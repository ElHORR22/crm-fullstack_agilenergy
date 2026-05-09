package com.example.bstagepfe.controllers;

import com.example.bstagepfe.DTO.LoginRequest;
import com.example.bstagepfe.entities.Utilisateur;
import com.example.bstagepfe.repos.UtilisateurRepository;
import com.example.bstagepfe.services.AuthService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private JavaMailSender mailSender;

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

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        Optional<Utilisateur> userOpt = utilisateurRepository.findByEmail(email);

        if (userOpt.isPresent()) {
            Utilisateur user = userOpt.get();

            String token = UUID.randomUUID().toString();
            user.setResetToken(token);

            user.setTokenExpiration(LocalDateTime.now().plusMinutes(15));
            utilisateurRepository.save(user);

            sendResetEmail(user.getEmail(), token);
        }

        return ResponseEntity.ok().body(Map.of("message", "Si cet email existe, un lien a été envoyé."));
    }

    private void sendResetEmail(String email, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Réinitialisation de votre mot de passe - AgilEnergy");
        message.setText("Cliquez ici pour changer votre mot de passe : " +
                "http://localhost:4200/reset-password?token=" + token);
        mailSender.send(message);
    }
}
