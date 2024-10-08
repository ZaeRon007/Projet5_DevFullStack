package com.openclassrooms.starterjwt.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import com.openclassrooms.starterjwt.repository.UserRepository;


@SpringBootTest
@AutoConfigureMockMvc
@Tag("AuthControllerIntegrationTest")
@DisplayName("integration tests for AuthController")
@ActiveProfiles("test")
public class AuthControllerIntegrationTest {
    
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    @Sql({"/script.sql"})
    void setup() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("should register a new user")
    public void shouldRegisterUser() throws Exception{
        // register
        String authRequest = "{ \"email\": \"" + "jd@gmail.com" + "\", \"firstName\": \"" + "john" + "\", \"lastName\": \"" + "doe" + "\", \"password\": \"" + "superpassword" + "\" }";

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authRequest))
                        .andExpect(status().isOk());
    }

    @Test
    @DisplayName("should login user")
    public void shouldLoginUser() throws Exception{
        //register
        String authRequest = "{ \"email\": \"" + "jd@gmail.com" + "\", \"firstName\": \"" + "john" + "\", \"lastName\": \"" + "doe" + "\", \"password\": \"" + "superpassword" + "\" }";

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authRequest))
                        .andExpect(status().isOk());
        

        // login
        String loginRequest = "{ \"email\": \"" + "jd@gmail.com" + "\", \"password\": \"" + "superpassword" + "\" }";

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginRequest))
                .andExpect(status().isOk());
    }

}
