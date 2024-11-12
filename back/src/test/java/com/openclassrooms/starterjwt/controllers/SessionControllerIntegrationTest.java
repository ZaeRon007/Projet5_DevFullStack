package com.openclassrooms.starterjwt.controllers;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import com.jayway.jsonpath.JsonPath;
import com.openclassrooms.starterjwt.repository.UserRepository;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.context.ActiveProfiles;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@SpringBootTest
@AutoConfigureMockMvc
@Tag("SessionControllerIntegrationTest")
@DisplayName("integration tests for SessionController")
@TestInstance(Lifecycle.PER_CLASS)
@ActiveProfiles("test")
public class SessionControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    private String token;

    @BeforeAll
    void setup() throws Exception {        
        // login
        String loginRequest = "{" + 
                            "\"email\": \"tyty@gmail.com\"," + 
                            "\"password\": \"test!1234\"" +
                            "}";

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
        // create session
        String sessionRequest = "{ \"name\": \"session\"," +
                                "   \"date\": \"2024-09-30\"," + 
                                "   \"teacher_id\": \"1\"," + 
                                "   \"description\": \"superbe description\" }";

        mockMvc.perform(post("/api/session/")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(sessionRequest))
                        .andExpect(status().isOk())
                        .andReturn();
    }

    @Test
    @DisplayName("should update session by id")
    public void shouldUpdateSessionById() throws Exception{
        // update session
        String sessionUpdateRequest = "{" +
                                "    \"name\": \"Session pour les pros UPDATED\"," +
                                "    \"date\": \"2023-12-01\"," +
                                "    \"teacher_id\": 1," +
                                "    \"users\": null," +
                                "    \"description\": \"Session pour les pros\"" +
                                "}";

        mockMvc.perform(put("/api/session/1")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(sessionUpdateRequest))
                        .andExpect(status().isOk());
    }

    @Test
    @DisplayName("should delete session by id")
    public void shouldDeleteSessionById() throws Exception{
        // delete session
        mockMvc.perform(delete("/api/session/2")
                        .header("Authorization", "Bearer " + token))
                        .andExpect(status().isOk());
    }

    @Test
    @DisplayName("should participate to session")
    public void shouldParticipateToSessionAndNoLongerParticipate() throws Exception{
        int sessionId = 3; 

        Long userId = userRepository.findByEmail("toto@gmail.com").get().getId();

        // participate to session
        String participateRequest = "{ \"id\": \"" + sessionId + "\", \"userId\": \"" + userId + "\" }";

        mockMvc.perform(post("/api/session/" + sessionId + "/participate/" + userId)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(participateRequest))
                        .andExpect(status().isOk())
                        .andReturn();
        
        // no longer participate
        mockMvc.perform(delete("/api/session/" + sessionId + "/participate/" + userId)
                                        .header("Authorization", "Bearer " + token)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(participateRequest))
                                        .andExpect(status().isOk())
                                        .andReturn();

    }

    @Test
    @DisplayName("should find by id to session")
    public void shouldFindByIdToSession() throws Exception{
        int sessionId = 4; 

        // find session by id
        String findRequest = "{ \"id\": \"" + sessionId + "\" }";

        mockMvc.perform(get("/api/session/" + sessionId)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(findRequest))
                        .andExpect(status().isOk())
                        .andReturn();
    }

    @Test
    @DisplayName("should find all")
    public void shouldFindAllSessions() throws Exception{
        // create session 1 
        String sessionRequest1 = "{ \"name\": \"session1\"," +  
                                    "\"date\": \"2024-09-30\"," +  
                                    "\"teacher_id\": \"1\"," +  
                                    "\"description\": \"superbe description\" }";

        mockMvc.perform(post("/api/session/")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(sessionRequest1))
                        .andExpect(status().isOk())
                        .andReturn();

        // create session 2
        String sessionRequest2 = "{ \"name\": \"session2\"," +  
                                    "\"date\": \"2024-10-01\"," +  
                                    "\"teacher_id\": \"1\"," +  
                                    "\"description\": \"superbe description\" }";

        mockMvc.perform(post("/api/session/")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(sessionRequest2))
                        .andExpect(status().isOk())
                        .andReturn();
                        
        // create session 3
        String sessionRequest3 = "{ \"name\": \"session3\"," +  
                                    "\"date\": \"2024-10-05\"," +  
                                    "\"teacher_id\": \"1\"," +  
                                    "\"description\": \"superbe description\" }";

        mockMvc.perform(post("/api/session/")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(sessionRequest3))
                        .andExpect(status().isOk())
                        .andReturn();

        // find all sessions
        mockMvc.perform(get("/api/session/")
                        .header("Authorization", "Bearer " + token))
                        .andExpect(status().isOk())
                        .andReturn();
    }
    
}
