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
    void testAccesPublic_DevraitEtreInterditSansToken() throws Exception {
        mockMvc.perform(get("/api/dashboard/chiffre-affaires"))
                .andExpect(status().isForbidden()); // Doit retourner 403
    }

    @Test
    @WithMockUser(roles = "ADMIN")
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
}
