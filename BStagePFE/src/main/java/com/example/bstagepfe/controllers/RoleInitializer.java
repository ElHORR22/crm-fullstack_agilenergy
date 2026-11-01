package com.example.bstagepfe.controllers;
import com.example.bstagepfe.entities.Role;
import com.example.bstagepfe.entities.RoleType;
import com.example.bstagepfe.entities.Utilisateur;
import com.example.bstagepfe.repos.RoleRepository;
import com.example.bstagepfe.repos.UtilisateurRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

// CommandLineRunner (et non @PostConstruct) : avec spring.main.lazy-initialization=true,
// un @Component que personne n'injecte n'est jamais instancié, alors que les runners
// sont toujours exécutés au démarrage.
@Component
public class RoleInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(RoleInitializer.class);

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${app.admin.email:}")
    private String adminEmail;

    @Value("${app.admin.password:}")
    private String adminPassword;

    @Override
    public void run(String... args) {
        initRoles();
        initAdmin();
    }

    private void initRoles() {
        for (RoleType roleType : RoleType.values()) {
            if (roleRepository.findByName(roleType) == null) {
                Role role = new Role();
                role.setName(roleType);
                roleRepository.save(role);
            }
        }
    }

    // Crée le premier administrateur si aucun utilisateur n'existe encore :
    // sans lui, personne ne peut se connecter pour créer les autres comptes.
    private void initAdmin() {
        if (utilisateurRepository.count() > 0) {
            return;
        }
        if (adminEmail.isBlank() || adminPassword.isBlank()) {
            log.warn("Aucun utilisateur en base : définir ADMIN_EMAIL et ADMIN_PASSWORD pour créer le premier administrateur.");
            return;
        }

        Utilisateur admin = new Utilisateur();
        admin.setEmail(adminEmail);
        admin.setMdp(passwordEncoder.encode(adminPassword));
        admin.setNom("Administrateur");
        admin.setPrenom("");
        admin.setActif(true);
        admin.setRole(roleRepository.findByName(RoleType.ADMINISTRATEUR));
        utilisateurRepository.save(admin);
        log.info("Administrateur initial créé : {}", adminEmail);
    }
}
