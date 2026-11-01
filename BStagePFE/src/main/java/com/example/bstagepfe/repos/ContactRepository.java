package com.example.bstagepfe.repos;
import com.example.bstagepfe.entities.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface ContactRepository extends JpaRepository<Contact, Long> {
    Optional<Contact> findContactById(Long id);

    // L'identifiant de Client s'appelle idC : requête explicite plutôt que findByClientId.
    @Query("SELECT c FROM Contact c WHERE c.client.idC = :clientId AND c.isDeleted = false")
    List<Contact> findActifsByClientId(@Param("clientId") Long clientId);

    List<Contact> findByIsDeletedFalse();
}
