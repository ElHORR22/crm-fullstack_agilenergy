package com.example.bstagepfe;

import com.example.bstagepfe.entities.Utilisateur;
import com.example.bstagepfe.repos.UtilisateurRepository;
import com.example.bstagepfe.services.JWTService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthFilter.class);

    // ✅ FIX 5 : N'injecte plus directement le secret — délègue à JWTService
    // FIX original : @Value("${app.jwt.secret=}") permettait une valeur vide dangereuse
    private final JWTService jwtService;
    private final UtilisateurRepository utilisateurRepository;

    public JwtAuthFilter(JWTService jwtService, UtilisateurRepository utilisateurRepository) {
        this.jwtService = jwtService;
        this.utilisateurRepository = utilisateurRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring(7);

        try {
            // ✅ FIX 7 : Délègue la validation à JWTService (séparation des responsabilités)
            Claims claims = jwtService.validateAndExtractClaims(token);

            String email = claims.getSubject();

            // Le compte est relu à chaque requête : un utilisateur désactivé ou supprimé
            // perd l'accès immédiatement, et un changement de rôle s'applique sans attendre
            // l'expiration du jeton (24 h).
            Utilisateur user = utilisateurRepository.findByEmail(email).orElse(null);
            if (user != null && user.isActif() && user.getRole() != null && user.getRole().getName() != null) {
                var auth = new UsernamePasswordAuthenticationToken(
                        email,
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().getName().name()))
                );
                SecurityContextHolder.getContext().setAuthentication(auth);
            } else {
                log.warn("JWT refusé pour [{}] : compte inexistant, inactif ou sans rôle", email);
            }

        } catch (JwtException e) {
            // ✅ FIX 4 : Le catch n'est plus silencieux — log du problème pour le débogage
            // On ne bloque pas la requête ici (elle sera rejetée par Spring Security
            // car le contexte d'auth restera vide), mais on trace l'incident.
            log.warn("JWT invalide pour [{}] : {}", request.getRequestURI(), e.getMessage());
        } catch (Exception e) {
            log.error("Erreur inattendue lors de la validation JWT : {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}
