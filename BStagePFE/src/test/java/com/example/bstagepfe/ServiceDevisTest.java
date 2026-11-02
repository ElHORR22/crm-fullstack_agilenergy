package com.example.bstagepfe;

import com.example.bstagepfe.DTO.*;
import com.example.bstagepfe.entities.*;
import com.example.bstagepfe.repos.*;
import com.example.bstagepfe.services.DevisCalculatorService;
import com.example.bstagepfe.services.ServiceDevis;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServiceDevisTest {

    @Mock private DevisRepository devisRepository;
    @Mock private ClientRepository clientRepository;
    @Mock private ProspectRepository prospectRepository;
    @Mock private TVARepository tvaRepository;
    @Mock private DevisCalculatorService calculator;

    @InjectMocks
    private ServiceDevis serviceDevis;

    private DevisRequestDTO devisDto;
    private TVA tva;

    @BeforeEach
    void setUp() {
        tva = new TVA();
        tva.setId(1L);
        tva.setValeur(19);

        devisDto = new DevisRequestDTO();
        devisDto.setSujetDevis("Devis Test AgilEnergy");
        devisDto.setClientId(1L);

        LigneDevisRequestDTO ligneDto = new LigneDevisRequestDTO();
        ligneDto.setLibelleProduit("Produit Solaire");
        ligneDto.setQuantite(BigDecimal.valueOf(10));
        ligneDto.setPrixUnitaireHT(BigDecimal.valueOf(100));
        devisDto.setLigneDevis(Collections.singletonList(ligneDto));
    }

    @Test
    void testAddDevisFromDto_PourClient_Succes() {
        Client client = new Client();
        client.setId(1L);
        client.setNom("Client Test");

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(tvaRepository.findById(1L)).thenReturn(Optional.of(tva));
        when(devisRepository.save(any(Devis.class))).thenAnswer(invocation -> {
            Devis d = invocation.getArgument(0);
            d.setId(100L);
            return d;
        });

        DevisResponseDTO response = serviceDevis.addDevisFromDto(devisDto);

        assertNotNull(response);
        assertEquals(100L, response.getId());
        assertEquals("Client Test", response.getNomClient());
        verify(calculator, times(1)).compute(any(Devis.class));
        verify(devisRepository).save(any(Devis.class));
    }

    @Test
    void testAddDevisFromDto_DoitLancerExceptionSiClientInexistant() {
        when(clientRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            serviceDevis.addDevisFromDto(devisDto);
        });
    }

    // ---------- Statuts ----------

    private Devis devisAvecStatut(StatutDevis statut, String emailClient) {
        Client client = new Client();
        client.setId(1L);
        client.setEmail(emailClient);
        Devis d = new Devis();
        d.setId(7L);
        d.setStatut(statut);
        d.setClient(client);
        d.setLigneDevis(new java.util.ArrayList<>());
        return d;
    }

    @Test
    void nouveauDevis_EstUnBrouillon() {
        Client client = new Client();
        client.setId(1L);
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(tvaRepository.findById(1L)).thenReturn(Optional.of(tva));
        when(devisRepository.save(any(Devis.class))).thenAnswer(i -> i.getArgument(0));

        DevisResponseDTO response = serviceDevis.addDevisFromDto(devisDto);

        assertEquals(StatutDevis.BROUILLON, response.getStatut());
        assertTrue(response.getTransitionsPossibles().contains(StatutDevis.ENVOYE));
    }

    @Test
    void modifierUnDevisEnvoye_EstRefuse() {
        when(devisRepository.findById(7L)).thenReturn(Optional.of(devisAvecStatut(StatutDevis.ENVOYE, "a@b.tn")));

        assertThrows(IllegalStateException.class, () -> serviceDevis.updateDevisFromDto(7L, devisDto));
        verify(devisRepository, never()).save(any());
    }

    @Test
    void supprimerUnDevisAccepte_EstRefuse() {
        when(devisRepository.findById(7L)).thenReturn(Optional.of(devisAvecStatut(StatutDevis.ACCEPTE, "a@b.tn")));

        assertThrows(IllegalStateException.class, () -> serviceDevis.deleteDevis(7L));
        verify(devisRepository, never()).delete(any());
    }

    @Test
    void envoyerUnBrouillon_ChangeLeStatutEtLaDate() {
        Devis devis = devisAvecStatut(StatutDevis.BROUILLON, "a@b.tn");
        when(devisRepository.findById(7L)).thenReturn(Optional.of(devis));
        when(devisRepository.save(any(Devis.class))).thenAnswer(i -> i.getArgument(0));

        DevisResponseDTO response = serviceDevis.changerStatut(7L, StatutDevis.ENVOYE);

        assertEquals(StatutDevis.ENVOYE, response.getStatut());
        assertEquals(java.time.LocalDate.now(), response.getDateStatut());
    }

    @Test
    void accepterUnBrouillon_TransitionInterdite() {
        when(devisRepository.findById(7L)).thenReturn(Optional.of(devisAvecStatut(StatutDevis.BROUILLON, "a@b.tn")));

        assertThrows(IllegalStateException.class, () -> serviceDevis.changerStatut(7L, StatutDevis.ACCEPTE));
    }

    @Test
    void clientAccepteSonDevisEnvoye() {
        Devis devis = devisAvecStatut(StatutDevis.ENVOYE, "client@agil.tn");
        when(devisRepository.findById(7L)).thenReturn(Optional.of(devis));
        when(devisRepository.save(any(Devis.class))).thenAnswer(i -> i.getArgument(0));

        DevisResponseDTO response = serviceDevis.repondreClient(7L, "client@agil.tn", StatutDevis.ACCEPTE);

        assertEquals(StatutDevis.ACCEPTE, response.getStatut());
    }

    @Test
    void clientNePeutPasRepondreAuDevisDUnAutre() {
        when(devisRepository.findById(7L)).thenReturn(Optional.of(devisAvecStatut(StatutDevis.ENVOYE, "autre@agil.tn")));

        assertThrows(ResponseStatusException.class,
                () -> serviceDevis.repondreClient(7L, "client@agil.tn", StatutDevis.ACCEPTE));
    }

    @Test
    void clientNePeutPasRepasserEnBrouillon() {
        assertThrows(IllegalArgumentException.class,
                () -> serviceDevis.repondreClient(7L, "client@agil.tn", StatutDevis.BROUILLON));
    }
}
