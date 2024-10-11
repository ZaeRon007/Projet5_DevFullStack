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
@Tag("UserControllerIntegrationTest")
@DisplayName("integration tests for UserController")
@TestInstance(Lifecycle.PER_CLASS)
@ActiveProfiles("test")
public class UserControllerIntegrationTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private UserRepository userRepository;

    private String token;

    @BeforeAll
    void setup() throws Exception {     
        // login
        String loginRequest =  "{" + 
                                "   \"email\": \"tyty@gmail.com\"," + 
                                "   \"password\": \"test!1234\"" +
                                " }";

        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequest))
                        .andExpect(status().isOk())
                        .andReturn();
        
        String response = result.getResponse().getContentAsString();
        token = JsonPath.parse(response).read("$.token");
    }

    @Test
    @DisplayName("should get a user by id")
    public void shouldGetUserById() throws Exception {
        int userId = 2;

        // get user 
        mockMvc.perform(get("/api/user/" + userId)
                        .header("Authorization", "Bearer " + token))
                        .andExpect(status().isOk());
    }

    @Test
    @DisplayName("should delete user by id")
    public void shouldDeleteUserById() throws Exception{
        String requestBodyLoginUser = "{" +
                                "    \"email\": \"tata@gmail.com\"," +
                                "    \"password\": \"test!1234\"" +
                                "}";

        MvcResult resultLogin = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBodyLoginUser))
                        .andReturn();

        String tokenForUserToDelete = JsonPath.read(resultLogin.getResponse().getContentAsString(), "$.token");

        Long userId = userRepository.findByEmail("tata@gmail.com").get().getId();

        String deleteRequest = "{ \"id\": \"" + userId + "\" }";

        // delete user 
        mockMvc.perform(delete("/api/user/" + userId)
                        .header("Authorization", "Bearer " + tokenForUserToDelete)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(deleteRequest))
                        .andExpect(status().isOk());
    }


}
