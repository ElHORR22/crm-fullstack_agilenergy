package com.example.bstagepfe.services;
import com.example.bstagepfe.entities.Client;
import com.example.bstagepfe.entities.Gouvernorat;
import com.example.bstagepfe.entities.Prospect;
import com.example.bstagepfe.entities.SecteurActivite;
import com.example.bstagepfe.repos.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class ServiceProspect {

    @Autowired
    private ProspectRepository prospectRepository;

    @Autowired
    private SecteurActiviteRepository secteurActiviteRepository;

    @Autowired
    private SourceProspectionRepository sourceProspectionRepository;

    @Autowired
    private GouvernoratRepository gouvernoratRepository;

    @Autowired
    private ClientRepository clientRepository;


    public List<Prospect> getAllProspects() {
        return prospectRepository.findByIsDeletedFalse();
    }

    public List<Prospect> getProspectById(Long idP) {
        return prospectRepository.findByIdP(idP);
    }

    public List<Prospect> getProspectBySecteurActivite(SecteurActivite secteurActivite) {
        return prospectRepository.findBySecteurActivite(secteurActivite);
    }

    public List<Prospect> getProspectByGouvernorat(Gouvernorat gouvernorat ) {
        return prospectRepository.findByGouvernorat(gouvernorat);
    }

    public List<Prospect> getProspectByEmail(String emailP) {
        return prospectRepository.findByEmailP(emailP);
    }

    public List<Prospect> getProspectByTelephone(int telephoneP) {
        return prospectRepository.findByTelephoneP(telephoneP);
    }

    public List<Prospect> getProspectsByNom(String nomP) {
        return prospectRepository.findByNomP(nomP);
    }

    public Prospect addProspect(@RequestBody Prospect prospect) {
        if (prospect.getGouvernorat() != null && prospect.getGouvernorat().getId() != null) {
            gouvernoratRepository.findById(prospect.getGouvernorat().getId())
                    .ifPresent(prospect::setGouvernorat);
        }

        if (prospect.getSourceProspection() != null && prospect.getSourceProspection().getId() != null) {
            sourceProspectionRepository.findById(prospect.getSourceProspection().getId())
                    .ifPresent(prospect::setSourceProspection);
        }

        if (prospect.getSecteurActivite() != null && prospect.getSecteurActivite().getId() != null) {
            secteurActiviteRepository.findById(prospect.getSecteurActivite().getId())
                    .ifPresent(prospect::setSecteurActivite);
        }

        return prospectRepository.save(prospect);
    }


    public Prospect updateProspect(Long idP, Prospect prospectDetails) {
        return prospectRepository.findById(idP).map(prospect -> {
            prospect.setNom(prospectDetails.getNom());
            prospect.setSociete(prospectDetails.getSociete());
            prospect.setAdresse(prospectDetails.getAdresse());
            prospect.setVille(prospectDetails.getVille());
            prospect.setTitre(prospectDetails.getTitre());
            prospect.setStatutprospect(prospectDetails.getStatutprospect());
            prospect.setSiteWeb(prospectDetails.getSiteWeb());
            prospect.setPays(prospectDetails.getPays());
            prospect.setMobile(prospectDetails.getMobile());
            prospect.setFax(prospectDetails.getFax());
            prospect.setEffectif(prospectDetails.getEffectif());
            prospect.setDateInscription(prospectDetails.getDateInscription());
            prospect.setDateModification(prospectDetails.getDateModification());
            prospect.setCodePostal(prospectDetails.getCodePostal());
            prospect.setAutremail(prospectDetails.getAutremail());
            prospect.setChiffreA(prospectDetails.getChiffreA());
            prospect.setEmail(prospectDetails.getEmail());
            prospect.setTelephone(prospectDetails.getTelephone());
            prospect.setIsDeleted(prospectDetails.getIsDeleted());

            if (prospectDetails.getGouvernorat() != null && prospectDetails.getGouvernorat().getId() != null) {
                gouvernoratRepository.findById(prospectDetails.getGouvernorat().getId())
                        .ifPresent(prospect::setGouvernorat);
            }

            if (prospectDetails.getSourceProspection() != null && prospectDetails.getSourceProspection().getId() != null) {
                sourceProspectionRepository.findById(prospectDetails.getSourceProspection().getId())
                        .ifPresent(prospect::setSourceProspection);
            }

            if (prospectDetails.getSecteurActivite() != null && prospectDetails.getSecteurActivite().getId() != null) {
                secteurActiviteRepository.findById(prospectDetails.getSecteurActivite().getId())
                        .ifPresent(prospect::setSecteurActivite);
            }

            return prospectRepository.save(prospect);
        }).orElseThrow(() -> new RuntimeException("Prospect non trouvé !"));
    }


    @Transactional
    public void deleteProspect(Long idP) {
        Prospect prospect = prospectRepository.findById(idP)
                .orElseThrow(() -> new RuntimeException("Prospect non trouvé avec l'ID : " + idP));
        prospect.setIsDeleted(true);
        prospectRepository.save(prospect);
    }

    public Client convertirEnClient(Prospect prospect) {
        Client client = new Client();
        client.setNom(prospect.getNom());
        client.setAdresse(prospect.getAdresse());
        client.setTelephone(prospect.getTelephone());
        client.setSecteurActivite(prospect.getSecteurActivite());
        client.setGouvernorat(prospect.getGouvernorat());
        client.setCodePostal(prospect.getCodePostal());
        client.setVille(prospect.getVille());
        client.setPays(prospect.getPays());
        client.setSiteWeb(prospect.getSiteWeb());
        client.setDateInscription(prospect.getDateInscription());
        client.setDateModification(prospect.getDateModification());
        client.setFax(prospect.getFax());
        client.setEmail(prospect.getEmail());
        client.setChiffreA(prospect.getChiffreA());
        client.setEffectif(prospect.getEffectif());
        client.setAutremail(prospect.getAutremail());

        return clientRepository.save(client);
    }

}

