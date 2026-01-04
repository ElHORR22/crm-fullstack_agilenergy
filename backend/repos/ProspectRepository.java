package com.example.bstagepfe.repos;
import com.example.bstagepfe.entities.SecteurActivite;
import com.example.bstagepfe.entities.Gouvernorat;
import com.example.bstagepfe.entities.Prospect;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProspectRepository extends JpaRepository<Prospect, Long> {
    List<Prospect> findBySecteurActivite(SecteurActivite secteurActivite);
    List<Prospect> findByIdP(Long idP);
    List<Prospect> findByGouvernorat(Gouvernorat gouvernorat);
    List<Prospect> findByEmailP(String emailP);
    List<Prospect> findByTelephoneP(int telephoneP);
    List<Prospect> findByNomP(String nomP);
    List<Prospect> findByIsDeletedFalse();
}


