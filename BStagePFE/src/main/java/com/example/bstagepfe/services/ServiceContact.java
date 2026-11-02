package com.example.bstagepfe.services;
import jakarta.persistence.EntityNotFoundException;
import com.example.bstagepfe.entities.Client;
import com.example.bstagepfe.entities.Contact;
import com.example.bstagepfe.repos.ClientRepository;
import com.example.bstagepfe.repos.ContactRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ServiceContact {

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private ClientRepository clientRepository;

    public List<Contact> getContactsByClientId(Long clientId) {
        return contactRepository.findActifsByClientId(clientId);
    }

    public List<Contact> getAllContacts() {
        return contactRepository.findByIsDeletedFalse();
    }

    public Contact addContact(Contact contact) {
        if (contact.getClientId() == null) {
            throw new IllegalArgumentException("Le client du contact est obligatoire");
        }
        Client client = clientRepository.findById(contact.getClientId())
                .orElseThrow(() -> new EntityNotFoundException("Client non trouvé avec l'ID : " + contact.getClientId()));
        contact.setClient(client);
        return contactRepository.save(contact);
    }

    public Contact updateContact(Long id, Contact detailscontact) {
        return contactRepository.findContactById(id).map(contact -> {
            contact.setNom(detailscontact.getNom());
            contact.setPrenom(detailscontact.getPrenom());
            contact.setFonction(detailscontact.getFonction());
            contact.setMobile(detailscontact.getMobile());
            contact.setTelephone(detailscontact.getTelephone());
            contact.setFax(detailscontact.getFax());
            contact.setEmail(detailscontact.getEmail());
            contact.setEmailSecondaire(detailscontact.getEmailSecondaire());
            contact.setSiteWeb(detailscontact.getSiteWeb());
            contact.setAdresse(detailscontact.getAdresse());
            contact.setCodePostal(detailscontact.getCodePostal());
            contact.setVille(detailscontact.getVille());
            contact.setPays(detailscontact.getPays());
            // Le client, isDeleted et les dates ne sont pas modifiables par cette route.

            return contactRepository.save(contact);
        }).orElseThrow(() -> new EntityNotFoundException("Contact non trouvé !"));
    }

    @Transactional
    public void deleteContact(Long id) {
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Contact non trouvé avec l'ID : " + id));
        contact.setIsDeleted(true);
        contactRepository.save(contact);
    }
}

