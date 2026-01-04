package com.example.bstagepfe.repos;
import com.example.bstagepfe.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByIdC(Long idC);
    Optional<Client> findByEmailC(String emailC);
    Optional<Client> findByTelephoneC(int telephoneC);
    List<Client> findByNomC(String nomC);
    List<Client> findByIsDeletedFalse();

}

