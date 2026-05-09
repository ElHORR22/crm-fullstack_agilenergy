package com.example.bstagepfe.controllers;
import com.example.bstagepfe.services.ServiceClient;
import com.example.bstagepfe.entities.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    @Autowired
    private ServiceClient Serviceclient;

    @GetMapping
    public List<Client> getAllClients() {
        return Serviceclient.getAllClients();
    }

    @GetMapping("/{idC}")
    public Optional<Client> getClientById(@PathVariable Long idC) {
        return Serviceclient.getClientById(idC);
    }

    @GetMapping("/emailC/{emailC}")
    public Optional<Client> getClientByEmail(@PathVariable String emailC) {
        return Serviceclient.getClientByEmail(emailC);
    }

    @GetMapping("/telephoneC/{telephoneC}")
    public Optional<Client> getClientByTelephone(@PathVariable int telephoneC) {
        return Serviceclient.getClientByTelephone(telephoneC);
    }

    @GetMapping("/nomC/{nomC}")
    public List<Client> getClientsByNom(@PathVariable String nomC) {
        return Serviceclient.getClientsByNom(nomC);
    }

    @PostMapping
    public Client addClient(@RequestBody Client client) {
        return Serviceclient.addClient(client);
    }

    @PutMapping("/{idC}")
    public Client updateClient(@PathVariable Long idC, @RequestBody Client client) {
        return Serviceclient.updateClient(idC, client);
    }

    @DeleteMapping("/{idC}")
    public void deleteClient(@PathVariable Long idC) {
        Serviceclient.deleteClient(idC);
    }
}

