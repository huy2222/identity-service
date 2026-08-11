package com.devteria.identityservice.controller;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;

import com.devteria.identityservice.dto.request.UserCreationRequest;
import com.devteria.identityservice.dto.response.UserResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.extern.slf4j.Slf4j;
import org.testcontainers.junit.jupiter.Testcontainers;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class UserIntegrationControllerTest {

    @Container
    static final MySQLContainer<?> MY_SQL_CONTAINER = new MySQLContainer<>("mysql:8.0.33");

    @DynamicPropertySource
    static void configureDatasource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", MY_SQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", MY_SQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", MY_SQL_CONTAINER::getPassword);
        registry.add("spring.datasource.driverClassName", () -> "com.mysql.cj.jdbc.Driver");
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "update");
    }


    @Autowired
    private MockMvc mockMvc;

    private UserCreationRequest userCreationRequest;
    private UserResponse userResponse;
    private LocalDate dob;

    @BeforeEach
    void initData() {
        dob = LocalDate.of(1990, 1, 1);
        userCreationRequest = UserCreationRequest.builder()
                .username("join12345")
                .firstName("Test")
                .lastName("Huy")
                .password("123456781111")
                .dob(dob)
                .build();

        userResponse = UserResponse.builder()
                .id("cd5151e0-1c2b-4f3a-9d5e-123456789abc")
                .username("join12345")
                .firstName("Test")
                .lastName("Huy")
                .dob(dob)
                .build();
    }

    @Test
    void createUser_validRequest_success() throws Exception {
        // Given
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String content = objectMapper.writeValueAsString(userCreationRequest);

//         When, then
        var response = mockMvc.perform(MockMvcRequestBuilders.post("/users")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(content))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andExpect(MockMvcResultMatchers.jsonPath("code").value("1000"))

         .andExpect(MockMvcResultMatchers.jsonPath("result.username").value("join12345"))
         .andExpect(MockMvcResultMatchers.jsonPath("result.firstName").value("Test"))
         .andExpect(MockMvcResultMatchers.jsonPath("result.lastName").value("Huy"))
         .andExpect(MockMvcResultMatchers.jsonPath("result.dob").value(dob.toString()));

        log.info("Response: {}", response.andReturn().getResponse().getContentAsString());
    }
}
