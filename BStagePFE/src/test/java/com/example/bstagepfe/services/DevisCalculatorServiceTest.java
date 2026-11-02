package com.example.bstagepfe.services;

import com.example.bstagepfe.entities.Client;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DevisCalculatorServiceTest {

    private Client client(Boolean exonere, LocalDate limite) {
        Client c = new Client();
        c.setExonere(exonere);
        c.setDateLimiteExo(limite);
        return c;
    }

    @Test
    void prospectOuClientAbsent_NonExonere() {
        assertFalse(DevisCalculatorService.estExonere(null));
    }

    @Test
    void clientNonExonere_PayeLaTva() {
        assertFalse(DevisCalculatorService.estExonere(client(false, null)));
    }

    @Test
    void clientExonereSansDateLimite_Exonere() {
        assertTrue(DevisCalculatorService.estExonere(client(true, null)));
    }

    @Test
    void clientExonereAvecDateLimiteFuture_Exonere() {
        assertTrue(DevisCalculatorService.estExonere(client(true, LocalDate.now().plusDays(10))));
    }

    @Test
    void clientExonereAvecDateLimiteDepassee_PayeLaTva() {
        assertFalse(DevisCalculatorService.estExonere(client(true, LocalDate.now().minusDays(1))));
    }
}
