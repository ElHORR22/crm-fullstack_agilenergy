package com.example.bstagepfe;

import com.example.bstagepfe.entities.StatutDevis;
import org.junit.jupiter.api.Test;

import static com.example.bstagepfe.entities.StatutDevis.*;
import static org.junit.jupiter.api.Assertions.*;

class StatutDevisTest {

    @Test
    void brouillon_PeutSeulementEtreEnvoye() {
        assertTrue(BROUILLON.peutPasserA(ENVOYE));
        assertFalse(BROUILLON.peutPasserA(ACCEPTE));
        assertFalse(BROUILLON.peutPasserA(REFUSE));
    }

    @Test
    void envoye_PeutEtreAccepteRefuseOuRepasseEnBrouillon() {
        assertTrue(ENVOYE.peutPasserA(ACCEPTE));
        assertTrue(ENVOYE.peutPasserA(REFUSE));
        assertTrue(ENVOYE.peutPasserA(BROUILLON));
    }

    @Test
    void accepteEtRefuse_SontDefinitifs() {
        for (StatutDevis cible : StatutDevis.values()) {
            assertFalse(ACCEPTE.peutPasserA(cible), "ACCEPTE -> " + cible);
            assertFalse(REFUSE.peutPasserA(cible), "REFUSE -> " + cible);
        }
    }

    @Test
    void cibleNulle_Refusee() {
        assertFalse(BROUILLON.peutPasserA(null));
    }

    @Test
    void seulLeBrouillonEstModifiable_EtLeClientNeLeVoitPas() {
        assertTrue(BROUILLON.estModifiable());
        assertFalse(ENVOYE.estModifiable());
        assertFalse(BROUILLON.estVisibleParClient());
        assertTrue(ENVOYE.estVisibleParClient());
    }

    @Test
    void unDevisAccepteNePeutPasEtreSupprime() {
        assertTrue(BROUILLON.estSupprimable());
        assertTrue(REFUSE.estSupprimable());
        assertFalse(ENVOYE.estSupprimable());
        assertFalse(ACCEPTE.estSupprimable());
    }
}
