package com.example.bstagepfe.repos;
import com.example.bstagepfe.entities.Emballage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmballageRepository extends JpaRepository<Emballage, Long> {
    Optional<Emballage> findByCodeEmballage(Integer codeEmballage);
    List<Emballage> findByIsDeletedFalse();
    // findFirst : le code n'est pas unique en base, un doublon ne doit pas faire échouer le devis.
    Optional<Emballage> findFirstByCodeEmballageAndIsDeletedFalse(Integer codeEmballage);
}
