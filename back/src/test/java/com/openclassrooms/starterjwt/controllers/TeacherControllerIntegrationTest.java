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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.context.ActiveProfiles;
import org.junit.jupiter.api.TestInstance.Lifecycle;


@SpringBootTest
@AutoConfigureMockMvc
@Tag("TeacherControllerIntegrationTest")
@DisplayName("integration tests for TeacherController")
@TestInstance(Lifecycle.PER_CLASS)
@ActiveProfiles("test")
public class TeacherControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private String token;

    @BeforeAll
    public void setup() throws Exception{
        // login
        String loginRequest =  "{" + 
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
    @DisplayName("should find teacher by id")
    public void shouldFindTeacherById() throws Exception{
        Long id = 1L;

        // find teacher by id
        String findRequest = "{ \"id\": \"" + id + "\" }";

        mockMvc.perform(get("/api/teacher/" + id)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(findRequest))
                        .andExpect(status().isOk());
    }

    @Test
    @DisplayName("should find all teachers")
    public void shouldFindTeachers() throws Exception{
        // find teacher by id
        mockMvc.perform(get("/api/teacher/")
                        .header("Authorization", "Bearer " + token))
                        .andExpect(status().isOk());
    }
}
