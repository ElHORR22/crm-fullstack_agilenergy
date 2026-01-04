package com.example.bstagepfe.repos;
import com.example.bstagepfe.entities.Devis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DevisRepository extends JpaRepository<Devis, Long> {
    Optional<Devis> findDevisById(Long id);
    @Query("SELECT d FROM Devis d LEFT JOIN FETCH d.ligneDevis")
    List<Devis> findAllWithLignes();
}
