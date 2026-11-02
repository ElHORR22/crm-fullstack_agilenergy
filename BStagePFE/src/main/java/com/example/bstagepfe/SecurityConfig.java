package com.example.bstagepfe;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private static final String ADMIN = "ADMINISTRATEUR";
    private static final String COMMERCIAL = "COMMERCIAL";
    private static final String LOGISTICIEN = "LOGISTICIEN";
    private static final String CLIENT = "CLIENT";

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 401 = pas (ou plus) connecté, 403 = connecté mais rôle insuffisant :
                // le front déconnecte sur 401 et affiche « Accès refusé » sur 403.
                .exceptionHandling(ex -> ex.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                // Les règles reprennent les rôles des routes Angular : le guard côté front
                // n'est qu'une aide à la navigation, c'est ici que l'accès est réellement contrôlé.
                // L'ordre compte : la première règle qui correspond s'applique.
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(HttpMethod.POST,
                                "/api/auth/login", "/api/auth/forgot-password", "/api/auth/reset-password").permitAll()

                        .requestMatchers("/api/utilisateurs/**", "/api/roles/**", "/api/dashboard/**")
                            .hasRole(ADMIN)

                        // Le CLIENT ne voit que ses devis et ne peut qu'y répondre ;
                        // l'appartenance est vérifiée dans ServiceDevis / DevisController.
                        .requestMatchers(HttpMethod.GET, "/api/devis/mes-devis").hasRole(CLIENT)
                        .requestMatchers(HttpMethod.PUT, "/api/devis/*/reponse").hasRole(CLIENT)
                        .requestMatchers(HttpMethod.GET, "/api/devis/*/pdf").hasAnyRole(ADMIN, COMMERCIAL, CLIENT)

                        .requestMatchers("/api/clients/**", "/api/prospects/**", "/api/contacts/**",
                                "/api/devis/**", "/api/lignes-devis/**")
                            .hasAnyRole(ADMIN, COMMERCIAL)
                        .requestMatchers("/api/produits/**", "/api/emballages/**")
                            .hasAnyRole(ADMIN, COMMERCIAL, LOGISTICIEN)

                        // Référentiels : lecture pour tout utilisateur connecté, modification réservée à l'admin.
                        .requestMatchers(HttpMethod.GET, "/api/parametrage/**", "/api/gouvernorats/**",
                                "/api/secteurs/**", "/api/sourcesprospection/**").authenticated()
                        .requestMatchers("/api/parametrage/**", "/api/gouvernorats/**",
                                "/api/secteurs/**", "/api/sourcesprospection/**").hasRole(ADMIN)

                        .anyRequest().authenticated())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Frontend servi par ng serve et par le conteneur nginx (tous deux sur le port 4200).
        // Pour un déploiement en ligne, ajouter ici l'URL publique du frontend.
        configuration.setAllowedOrigins(List.of("http://localhost:4200"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of(
                "Authorization", "Content-Type", "Accept", "Origin", "X-Requested-With"));
        configuration.setExposedHeaders(List.of("Authorization"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
