package com.openclassrooms.starterjwt.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import com.jayway.jsonpath.JsonPath;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Tag("SessionControllerIntegrationTest")
@DisplayName("integration tests for SessionController")
public class SessionControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private UserRepository userRepository;

    private String token;

    @BeforeEach
    @Sql({"/script.sql"})
    void setup() throws Exception {
        sessionRepository.deleteAll();
        userRepository.deleteAll();

        String authRequest = "{ \"email\": \"" + "jd@gmail.com" + "\", \"firstName\": \"" + "john" + "\", \"lastName\": \"" + "doe" + "\", \"password\": \"" + "superpassword" + "\" }";

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authRequest));
        
        String loginRequest = "{ \"email\": \"" + "jd@gmail.com" + "\", \"password\": \"" + "superpassword" + "\" }";

        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequest))
                        .andExpect(status().isOk())
                        .andReturn();
        
        String response = result.getResponse().getContentAsString();
        token = JsonPath.parse(response).read("$.token");
    }

    @Test
    @DisplayName("should create session")
    public void shouldCreateSession() throws Exception{
        String sessionRequest = "{ \"name\": \"" + "session" + "\", \"date\": \"" + "2024-09-30" + "\", \"teacher_id\": \"" + "1" + "\", \"password\": \"" + "superpassword" + "\", \"description\": \"" + "superbe description" + "\", \"test\": \"" + "test" + "\"}";

        MvcResult result = mockMvc.perform(post("/api/session/")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(sessionRequest))
                        .andExpect(status().isOk())
                        .andReturn();

        String response = result.getResponse().getContentAsString();
        int id = JsonPath.parse(response).read("$.id"); 

        assertThat(id).isNotNull();
    }

    @Test
    @DisplayName("should update session by id")
    public void shouldUpdateSessionById() throws Exception{

        String sessionRequest = "{ \"name\": \"" + "session" + "\", \"date\": \"" + "2024-09-30" + "\", \"teacher_id\": \"" + "1" + "\", \"password\": \"" + "superpassword" + "\", \"description\": \"" + "superbe description" + "\", \"test\": \"" + "test" + "\"}";

        MvcResult result = mockMvc.perform(post("/api/session/")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(sessionRequest))
                        .andExpect(status().isOk())
                        .andReturn();

        String response = result.getResponse().getContentAsString();
        int id = JsonPath.parse(response).read("$.id"); 

        String sessionUpdateRequest = "{ \"name\": \"" + "sessionUpdated" + "\", \"date\": \"" + "2024-09-30" + "\", \"teacher_id\": \"" + "1" + "\", \"password\": \"" + "superpassword" + "\", \"description\": \"" + "superbe description" + "\", \"test\": \"" + "test" + "\"}";

        mockMvc.perform(put("/api/session/" + id)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(sessionUpdateRequest))
                        .andExpect(status().isOk());
    }

    @Test
    @DisplayName("should delete session by id")
    public void shouldDeleteSessionById() throws Exception{

        String sessionRequest = "{ \"name\": \"" + "session" + "\", \"date\": \"" + "2024-09-30" + "\", \"teacher_id\": \"" + "1" + "\", \"password\": \"" + "superpassword" + "\", \"description\": \"" + "superbe description" + "\", \"test\": \"" + "test" + "\"}";

        MvcResult result = mockMvc.perform(post("/api/session/")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(sessionRequest))
                        .andExpect(status().isOk())
                        .andReturn();

        String response = result.getResponse().getContentAsString();
        int id = JsonPath.parse(response).read("$.id"); 

        mockMvc.perform(delete("/api/session/" + id)
                        .header("Authorization", "Bearer " + token))
                        .andExpect(status().isOk());
    }


    
}
