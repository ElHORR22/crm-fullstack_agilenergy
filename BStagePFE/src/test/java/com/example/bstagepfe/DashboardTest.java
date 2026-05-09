package com.example.bstagepfe;

import com.example.bstagepfe.entities.Client;
import com.example.bstagepfe.entities.Prospect;
import com.example.bstagepfe.repos.ClientRepository;
import com.example.bstagepfe.repos.ProspectRepository;
import com.example.bstagepfe.repos.DevisRepository;
import com.example.bstagepfe.entities.Devis;
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
    @WithMockUser(roles = "ADMIN")
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
    @WithMockUser(roles = "ADMIN")
    void testChiffreAffairesTotal_SommeCorrecte() throws Exception {
        Devis d1 = new Devis();
        d1.setTotalTTC(new BigDecimal("2000.00"));
        devisRepository.save(d1);

        Devis d2 = new Devis();
        d2.setTotalTTC(new BigDecimal("1500.50"));
        devisRepository.save(d2);

        mockMvc.perform(get("/api/dashboard/chiffre-affaires"))
                .andExpect(status().isOk())
                .andExpect(content().string("3500.5"));
    }
}