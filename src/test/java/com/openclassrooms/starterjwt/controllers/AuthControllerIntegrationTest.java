package com.openclassrooms.starterjwt.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
@Tag("AuthControllerIntegrationTest")
@DisplayName("integration tests for AuthController")
@TestInstance(Lifecycle.PER_CLASS)
@ActiveProfiles("test")
public class AuthControllerIntegrationTest {
    
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("should register a new user")
    public void shouldRegisterUser() throws Exception{
        // register
        String authRequest = "{" +
                                "\"email\": \"pere.ducrass@gmail.com\"," +
                                "\"firstName\": \"pere\"," +
                                "\"lastName\": \"ducrass\"," +
                                "\"password\": \"Antonin!!!!\"" +
                                "}";

        MvcResult response = mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authRequest))
                        .andExpect(status().isOk())
                        .andReturn();
        assertThat(response.getResponse().getContentAsString().contains("User registered successfully!"));
    }

    @Test
    @DisplayName("should login user")
    public void shouldLoginUser() throws Exception{
        
        // login
        String loginRequest =  "{" +
                                "    \"email\": \"toto@gmail.com\"," +
                                "    \"password\": \"test!1234\"" +
                                "}";

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginRequest))
                .andExpect(status().isOk());
    }

}
