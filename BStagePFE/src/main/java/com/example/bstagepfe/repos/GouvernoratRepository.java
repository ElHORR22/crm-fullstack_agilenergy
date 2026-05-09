package com.example.bstagepfe.repos;
import com.example.bstagepfe.entities.Gouvernorat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface GouvernoratRepository extends JpaRepository<Gouvernorat, Long> {

    Optional<Gouvernorat> findByNom(String nom);
    void deleteByNom(String nom);

    Optional<Gouvernorat> findById(Long id);
}


