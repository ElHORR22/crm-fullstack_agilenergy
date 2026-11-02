package com.example.bstagepfe;

import com.example.bstagepfe.entities.Client;
import com.example.bstagepfe.entities.Prospect;
import com.example.bstagepfe.repos.ClientRepository;
import com.example.bstagepfe.repos.ProspectRepository;
import com.example.bstagepfe.repos.DevisRepository;
import com.example.bstagepfe.entities.Devis;
import com.example.bstagepfe.entities.StatutDevis;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.is;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class DashboardTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ProspectRepository prospectRepository;

    @Autowired
    private DevisRepository devisRepository;

    @Test
    @WithMockUser(roles = "ADMINISTRATEUR")
    void testGetClientsProspectsCount_DoitRetournerLesValeursInitiales() throws Exception {
        Client c = new Client();
        c.setNom("Entreprise Alpha");
        c.setIsDeleted(false);
        clientRepository.save(c);

        Prospect p = new Prospect();
        p.setNom("Prospect Beta");
        p.setIsDeleted(false);
        prospectRepository.save(p);

        mockMvc.perform(get("/api/dashboard/clients-prospects-count"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clients", is(1)))
                .andExpect(jsonPath("$.prospects", is(1)));
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATEUR")
    void testChiffreAffairesTotal_SeulsLesDevisAcceptesComptent() throws Exception {
        devisRepository.save(devis("2000.00", StatutDevis.ACCEPTE));
        devisRepository.save(devis("1500.50", StatutDevis.ACCEPTE));
        devisRepository.save(devis("9999.00", StatutDevis.BROUILLON));
        devisRepository.save(devis("8888.00", StatutDevis.REFUSE));

        mockMvc.perform(get("/api/dashboard/chiffre-affaires"))
                .andExpect(status().isOk())
                .andExpect(content().string("3500.5"));
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATEUR")
    void testDevisParStatut_TousLesStatutsPresents() throws Exception {
        devisRepository.save(devis("100.00", StatutDevis.ACCEPTE));
        devisRepository.save(devis("100.00", StatutDevis.ENVOYE));
        devisRepository.save(devis("100.00", StatutDevis.ENVOYE));

        mockMvc.perform(get("/api/dashboard/devis-par-statut"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.BROUILLON", is(0)))
                .andExpect(jsonPath("$.ENVOYE", is(2)))
                .andExpect(jsonPath("$.ACCEPTE", is(1)))
                .andExpect(jsonPath("$.REFUSE", is(0)));
    }

    private Devis devis(String totalTTC, StatutDevis statut) {
        Devis d = new Devis();
        d.setTotalTTC(new BigDecimal(totalTTC));
        d.setStatut(statut);
        return d;
    }
}