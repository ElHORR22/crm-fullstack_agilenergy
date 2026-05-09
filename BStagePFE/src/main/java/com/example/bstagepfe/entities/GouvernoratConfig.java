package com.example.bstagepfe.entities;
import com.example.bstagepfe.repos.GouvernoratRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.List;

@Configuration
public class GouvernoratConfig {

    @Bean
    CommandLineRunner initGouvernorats(GouvernoratRepository gouvernoratRepository) {
        return args -> {
            if (gouvernoratRepository.count() == 0) {
                List<Gouvernorat> gouvernorats = List.of(
                        new Gouvernorat(1L, "Ariana"),
                        new Gouvernorat(2L, "Béja"),
                        new Gouvernorat(3L, "Ben Arous"),
                        new Gouvernorat(4L, "Bizerte"),
                        new Gouvernorat(5L, "El Kef"),
                        new Gouvernorat(6L, "Gabes"),
                        new Gouvernorat(7L, "Gafsa"),
                        new Gouvernorat(8L, "Jendouba"),
                        new Gouvernorat(9L, "Kairouan"),
                        new Gouvernorat(10L, "Kasserine"),
                        new Gouvernorat(11L, "Kebili"),
                        new Gouvernorat(12L, "Mahdia"),
                        new Gouvernorat(13L, "Manouba"),
                        new Gouvernorat(14L, "Medenine"),
                        new Gouvernorat(15L, "Monastir"),
                        new Gouvernorat(16L, "Nabeul"),
                        new Gouvernorat(17L, "Sfax"),
                        new Gouvernorat(18L, "Sidi Bouzid"),
                        new Gouvernorat(19L, "Siliana"),
                        new Gouvernorat(20L, "Sousse"),
                        new Gouvernorat(21L, "Tataouine"),
                        new Gouvernorat(22L, "Tozeur"),
                        new Gouvernorat(23L, "Tunis"),
                        new Gouvernorat(24L, "Zaghouan")
                );
                gouvernoratRepository.saveAll(gouvernorats);
            }
        };
    }
}

