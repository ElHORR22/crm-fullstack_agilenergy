package com.example.bstagepfe.repos;
import com.example.bstagepfe.entities.Emballage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmballageRepository extends JpaRepository<Emballage, Long> {
    Optional<Emballage> findByCodeEmballage(Integer codeEmballage);
}
