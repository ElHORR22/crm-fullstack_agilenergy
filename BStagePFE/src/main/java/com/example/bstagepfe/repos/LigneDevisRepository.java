package com.example.bstagepfe.repos;
import com.example.bstagepfe.entities.LigneDevis;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LigneDevisRepository extends JpaRepository<LigneDevis, Long> {
    List<LigneDevis> findByDevisId(Long devisId);

    @Transactional
    @Modifying
    void deleteByDevisId(Long devisId);

}

