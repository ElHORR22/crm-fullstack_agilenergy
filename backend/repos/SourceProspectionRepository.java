package com.example.bstagepfe.repos;
import com.example.bstagepfe.entities.SourceProspection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface SourceProspectionRepository extends JpaRepository<SourceProspection, Long> {

    Optional<SourceProspection> findByNom(String nom);
    void deleteByNom(String nom);

}

