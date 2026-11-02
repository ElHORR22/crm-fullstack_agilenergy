package com.example.bstagepfe;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

// Profil test : base H2 en mémoire (src/test/resources/application-test.properties),
// le contexte se charge sans MySQL.
@SpringBootTest
@ActiveProfiles("test")
class BStagePfeApplicationTests {

    @Test
    void contextLoads() {
    }

}
