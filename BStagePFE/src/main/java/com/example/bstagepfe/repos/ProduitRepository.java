package com.example.bstagepfe.repos;
import com.example.bstagepfe.entities.Produit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProduitRepository extends JpaRepository<Produit, Long> {
    Optional<Produit> findProduitById(Long id);
    List<Produit> findByCodeProduitIgnoringCaseContaining(String codeProduit);
    Optional<Produit> findByCodeProduit(String codeProduit);
}
