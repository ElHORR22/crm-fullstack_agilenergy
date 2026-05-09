package com.example.bstagepfe.repos;
import com.example.bstagepfe.DTO.ProduitDemandes;
import com.example.bstagepfe.entities.Devis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DevisRepository extends JpaRepository<Devis, Long> {
    Optional<Devis> findDevisById(Long id);
    @Query("SELECT DISTINCT d FROM Devis d LEFT JOIN FETCH d.ligneDevis")
    List<Devis> findAllWithLignes();

    @Query("SELECT SUM(d.totalTTC) FROM Devis d")
    Double getChiffreAffairesTotal();

    @Query("SELECT COALESCE(SUM(d.totalTTC), 0) FROM Devis d WHERE YEAR(d.dateCreation) = :annee AND MONTH(d.dateCreation) = :mois")
    Double getChiffreAffairesParMois(int annee, int mois);

    @Query("SELECT FUNCTION('MONTH', d.dateCreation), SUM(d.totalTTC) FROM Devis d " +
            "WHERE d.dateCreation >= :startDate " +
            "GROUP BY FUNCTION('MONTH', d.dateCreation) ORDER BY FUNCTION('MONTH', d.dateCreation)")
    List<Object[]> getChiffreAffairesByMonth(@Param("startDate") LocalDate startDate);

    @Query(value = """
    SELECT DATE_FORMAT(d.date_creation, '%Y-%m') AS mois, COUNT(*) AS total
    FROM devis d
    WHERE d.date_creation >= DATE_SUB(CURDATE(), INTERVAL 12 MONTH)
    GROUP BY mois
    ORDER BY mois
""", nativeQuery = true)
    List<Object[]> findDevisCountByMonthLastYearNative();


    @Query("SELECT new com.example.bstagepfe.DTO.ProduitDemandes(ld.codeProduit, SUM(ld.quantite)) " +
            "FROM LigneDevis ld " +
            "GROUP BY ld.codeProduit " +
            "ORDER BY SUM(ld.quantite) DESC")
    List<ProduitDemandes> findTopProduitsDemandes();

    @Query("""
    SELECT d FROM Devis d 
    LEFT JOIN d.client c 
    LEFT JOIN d.prospect p 
    WHERE 
        ((c IS NOT NULL AND c.email = :email AND c.isDeleted = false) 
        OR 
        (p IS NOT NULL AND p.email = :email AND p.isDeleted = false))
""")
    List<Devis> findDevisByClientOrProspectEmail(@Param("email") String email);

}
