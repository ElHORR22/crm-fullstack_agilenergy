package com.example.bstagepfe;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testAccesPublic_DevraitEtreRefuseSansToken() throws Exception {
        mockMvc.perform(get("/api/dashboard/chiffre-affaires"))
                .andExpect(status().isUnauthorized()); // 401 : non authentifié
    }

    @Test
    @WithMockUser(roles = "ADMINISTRATEUR")
    void testAccesAdmin_DevraitEtreAutorise() throws Exception {
        mockMvc.perform(get("/api/dashboard/chiffre-affaires"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "CLIENT")
    void testAccesClientAuDashboard_DevraitEtreInterdit() throws Exception {

        mockMvc.perform(get("/api/dashboard/chiffre-affaires"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "CLIENT")
    void testAccesClientASesPropresDevis_DevraitEtreAutorise() throws Exception {
        mockMvc.perform(get("/api/devis/mes-devis"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "COMMERCIAL")
    void testAccesCommercialAuxUtilisateurs_DevraitEtreInterdit() throws Exception {
        mockMvc.perform(get("/api/utilisateurs"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "CLIENT")
    void testAccesClientALaListeDesClients_DevraitEtreInterdit() throws Exception {
        mockMvc.perform(get("/api/clients"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "CLIENT")
    void testAccesClientALaListeDesDevis_DevraitEtreInterdit() throws Exception {
        mockMvc.perform(get("/api/devis"))
                .andExpect(status().isForbidden());
    }

    @Test
    void testPdfDevis_DevraitExigerUneConnexion() throws Exception {
        mockMvc.perform(get("/api/devis/1/pdf"))
                .andExpect(status().isUnauthorized());
    }
}
