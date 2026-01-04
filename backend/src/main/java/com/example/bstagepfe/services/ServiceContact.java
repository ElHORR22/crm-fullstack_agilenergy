package com.example.bstagepfe.services;
import com.example.bstagepfe.entities.Contact;
import com.example.bstagepfe.repos.ContactRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ServiceContact {

    @Autowired
    private ContactRepository contactRepository;

    public List<Contact> getContactsByClientId( Long clientId) {
        return contactRepository.findByClientIdAndIsDeletedFalse(clientId);
    }

    public List<Contact> getAllContacts() {
        return contactRepository.findAll();
    }

    public Contact addContact(Contact contact) {
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
            contact.setDateCreation(detailscontact.getDateCreation());
            contact.setDateModification(detailscontact.getDateModification());
            contact.setIsDeleted(detailscontact.getIsDeleted());

            return contactRepository.save(contact);
        }).orElseThrow(() -> new RuntimeException("Contact non trouvé !"));
    }

    @Transactional
    public void deleteContact(Long id) {
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contact non trouvé avec l'ID : " + id));
        contact.setIsDeleted(true);
        contactRepository.save(contact);
    }
}

