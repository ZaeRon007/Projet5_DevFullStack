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
import com.openclassrooms.starterjwt.repository.UserRepository;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Tag("UserControllerIntegrationTest")
@DisplayName("integration tests for UserController")
public class UserControllerIntegrationTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private UserRepository userRepository;

    private String token;

    @BeforeEach
    @Sql({"/script.sql"})
    void setup() throws Exception {
        userRepository.deleteAll();

        // register 
        String authRequest = "{ \"email\": \"" + "jd@gmail.com" + "\", \"firstName\": \"" + "john" + "\", \"lastName\": \"" + "doe" + "\", \"password\": \"" + "superpassword" + "\" }";

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authRequest))
                        .andExpect(status().isOk());
        
        // login
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
    @DisplayName("should get a user by id")
    public void shouldGetUserById() throws Exception {
        Long userId = userRepository.findByEmail("jd@gmail.com").get().getId();

        String getRequest = "{ \"id\": \"" + userId + "\" }";

        // get user 
        mockMvc.perform(get("/api/user/" + userId)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getRequest))
                        .andExpect(status().isOk())
                        .andReturn();
    }

    @Test
    @DisplayName("should delete user by id")
    public void shouldDeleteUserById() throws Exception{
        Long userId = userRepository.findByEmail("jd@gmail.com").get().getId();

        String deleteRequest = "{ \"id\": \"" + userId + "\" }";

        // delete user 
        mockMvc.perform(delete("/api/user/" + userId)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(deleteRequest))
                        .andExpect(status().isOk())
                        .andReturn();
    }


}
