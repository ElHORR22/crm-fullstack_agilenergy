package com.example.bstagepfe.repos;

import com.example.bstagepfe.entities.TVA;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TVARepository extends JpaRepository<TVA, Long> {
    Optional<TVA> findByValeur(int valeur);
}

