package com.example.bstagepfe.services;
import com.example.bstagepfe.entities.Client;
import com.example.bstagepfe.repos.ClientRepository;
import com.example.bstagepfe.repos.GouvernoratRepository;
import com.example.bstagepfe.repos.SecteurActiviteRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;
import java.util.Optional;

@Service
public class ServiceClient {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private GouvernoratRepository gouvernoratRepository;

    @Autowired
    private SecteurActiviteRepository secteurActiviteRepository;

    public List<Client> getAllClients() {
        return clientRepository.findByIsDeletedFalse();
    }

    public Optional<Client> getClientById(Long idC) {
        return clientRepository.findByIdC(idC);
    }

    public Optional<Client> getClientByEmail(String emailC) {
        return clientRepository.findByEmailC(emailC);
    }

    public Optional<Client> getClientByTelephone(int telephoneC) {
        return clientRepository.findByTelephoneC(telephoneC);
    }

    public List<Client> getClientsByNom(String nomC) {
        return clientRepository.findByNomC(nomC);
    }

    public Client addClient(@RequestBody Client client) {

        if (client.getGouvernorat() != null && client.getGouvernorat().getId() != null) {
            gouvernoratRepository.findById(client.getGouvernorat().getId())
                    .ifPresent(client::setGouvernorat);
        }

        if (client.getSecteurActivite() != null && client.getSecteurActivite().getId() != null) {
            secteurActiviteRepository.findById(client.getSecteurActivite().getId())
                    .ifPresent(client::setSecteurActivite);
        }

        return clientRepository.save(client);
    }

    public Client updateClient(Long id, Client clientDetails) {
        return clientRepository.findById(id).map(client -> {
            client.setNom(clientDetails.getNom());
            client.setNumCompte(clientDetails.getNumCompte());
            client.setNumsousCompte(clientDetails.getNumsousCompte());
            client.setMatFiscal(clientDetails.getMatFiscal());
            client.setChiffreA(clientDetails.getChiffreA());
            client.setEffectif(clientDetails.getEffectif());
            client.setExonere(clientDetails.getExonere());
            client.setConverted(clientDetails.getConverted());
            client.setDateLimiteExo(clientDetails.getDateLimiteExo());
            client.setTypeprixAchat(clientDetails.getTypeprixAchat());
            client.setEmail(clientDetails.getEmail());
            client.setAutremail(clientDetails.getAutremail());
            client.setSiteWeb(clientDetails.getSiteWeb());
            client.setTelephone(clientDetails.getTelephone());
            client.setAutretelephone(clientDetails.getAutretelephone());
            client.setFax(clientDetails.getFax());
            client.setAdresse(clientDetails.getAdresse());
            client.setCodePostal(clientDetails.getCodePostal());
            client.setVille(clientDetails.getVille());
            client.setPays(clientDetails.getPays());
            client.setDateInscription(clientDetails.getDateInscription());
            client.setDateModification(clientDetails.getDateModification());
            client.setIsDeleted(clientDetails.getIsDeleted());
            client.setProspect(clientDetails.getProspect());

            if (clientDetails.getGouvernorat() != null && clientDetails.getGouvernorat().getId() != null) {
                gouvernoratRepository.findById(clientDetails.getGouvernorat().getId())
                        .ifPresent(client::setGouvernorat);
            }

            if (clientDetails.getSecteurActivite() != null && clientDetails.getSecteurActivite().getId() != null) {
                secteurActiviteRepository.findById(clientDetails.getSecteurActivite().getId())
                        .ifPresent(client::setSecteurActivite);
            }

            return clientRepository.save(client);
        }).orElseThrow(() -> new RuntimeException("Client non trouvé !"));
    }


    @Transactional
    public void deleteClient(Long idC) {
        Client client = clientRepository.findById(idC)
                .orElseThrow(() -> new RuntimeException("Client non trouvé avec l'ID : " + idC));
        client.setIsDeleted(true);
        clientRepository.save(client);
    }

}

