package com.devteria.identityservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource(locations = "classpath:test.properties")
@SpringBootTest
class IdentityServiceApplicationTests {

    @Test
    void contextLoads() {}
}
