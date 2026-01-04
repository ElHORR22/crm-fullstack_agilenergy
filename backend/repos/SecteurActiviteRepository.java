package com.example.bstagepfe.repos;
import com.example.bstagepfe.entities.SecteurActivite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface SecteurActiviteRepository extends JpaRepository<SecteurActivite, Long> {

    Optional<SecteurActivite> findByNom(String nom);
    void deleteByNom(String nom);
}

