package com.openclassrooms.starterjwt.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testifyproject.annotation.Application;

import com.jayway.jsonpath.JsonPath;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.WebSecurityConfig;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest //(classes = {Application.class, WebSecurityConfig.class})
@AutoConfigureMockMvc
@Disabled
public class UserControllerIntegrationTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private UserRepository userRepository;

    private String token;

    private User user;

    @BeforeEach
    @Sql({"/script.sql"})
    void setup() throws Exception {
        userRepository.deleteAll();

        user = new User();
        user.setFirstName("Jane");
        user.setLastName("Doe");
        user.setEmail("jane.doe@example.com");
        user.setPassword("superpassword");
        user = userRepository.save(user);

        token = obtainAccessToken(user.getEmail(), user.getPassword());
    }

    private String obtainAccessToken(String username, String password) throws Exception {
        String authRequest = "{ \"email\": \"" + username + "\", \"password\": \"" + password + "\" }";

        MvcResult result = mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(authRequest))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        return JsonPath.parse(response).read("$.token"); 
    }

    //REGISTER : 
    // @Test
    // public void testCreateUser() throws Exception {
    //     String userJson = "{ \"name\": \"John Doe\", \"email\": \"john.doe@example.com\" }";
    //     mockMvc.perform(post("/users")
    //             .contentType(MediaType.APPLICATION_JSON)
    //             .content(userJson))
    //             .andExpect(status().isCreated())
    //             .andExpect(jsonPath("$.id").isNumber())
    //             .andExpect(jsonPath("$.name").value("John Doe"))
    //             .andExpect(jsonPath("$.email").value("john.doe@example.com"));
    // }

    @Test
    public void testGetUserById() throws Exception {
        

        mockMvc.perform(get("/api/user/" + user.getId()).header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.name").value("Jane Doe"))
                .andExpect(jsonPath("$.email").value("jane.doe@example.com"));
    }
}
