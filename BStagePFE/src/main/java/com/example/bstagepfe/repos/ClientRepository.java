package com.example.bstagepfe.repos;
import com.example.bstagepfe.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByIdC(Long idC);
    Optional<Client> findByEmail(String email);
    Optional<Client> findByTelephoneC(int telephoneC);
    List<Client> findByNomC(String nomC);
    List<Client> findByIsDeletedFalse();

    @Query("SELECT c.gouvernorat.nom, COUNT(c) FROM Client c WHERE c.isDeleted = false GROUP BY c.gouvernorat.nom")
    List<Object[]> countClientsByGouvernorat();

    @Query("SELECT c.secteurActivite.nom, COUNT(c) FROM Client c WHERE c.isDeleted = false GROUP BY c.secteurActivite.nom")
    List<Object[]> countClientsBySecteurActivite();

    long countByIsDeletedFalse();
}