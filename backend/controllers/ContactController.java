package com.example.bstagepfe.controllers;
import com.example.bstagepfe.entities.Contact;
import com.example.bstagepfe.services.ServiceContact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/contacts")
public class ContactController {

    @Autowired
    private ServiceContact serviceContact;

    @GetMapping("/client/{clientId}")
    public List<Contact> getContactsByClientId(@PathVariable Long clientId) {
        return serviceContact.getContactsByClientId(clientId);
    }

    @GetMapping
    public List<Contact> getAllContacts() {
        return serviceContact.getAllContacts();
    }

    @PostMapping
    public Contact addContact(@RequestBody Contact contact) {
        return serviceContact.addContact(contact);
    }

    @PutMapping("/{id}")
    public Contact updateContact(@PathVariable Long id, @RequestBody Contact contact) {
        return serviceContact.updateContact(id, contact);
    }

    @DeleteMapping("/{id}")
    public void deleteContact(@PathVariable Long id) {
        serviceContact.deleteContact(id);
    }
}
