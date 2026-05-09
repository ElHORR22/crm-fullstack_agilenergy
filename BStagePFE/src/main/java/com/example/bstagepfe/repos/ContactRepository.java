package com.example.bstagepfe.repos;
import com.example.bstagepfe.entities.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ContactRepository extends JpaRepository<Contact, Long> {
    Optional<Contact> findContactById(Long id);
    List<Contact> findByClientIdAndIsDeletedFalse(Long clientId);
}
