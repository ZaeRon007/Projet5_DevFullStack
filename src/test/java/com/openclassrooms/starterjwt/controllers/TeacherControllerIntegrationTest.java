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
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@AutoConfigureMockMvc
@Tag("TeacherControllerIntegrationTest")
@DisplayName("integration tests for TeacherController")
@ActiveProfiles("test")
public class TeacherControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private UserRepository userRepository;

    private String token;

    @BeforeEach
    @Sql({"/script.sql"})
    public void setup() throws Exception{
        userRepository.deleteAll();
        teacherRepository.deleteAll();

        // register 
        String authRequest = "{ \"email\": \"" + "jd@gmail.com" + "\", \"firstName\": \"" + "john" + "\", \"lastName\": \"" + "doe" + "\", \"password\": \"" + "superpassword" + "\" }";

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authRequest));
        
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
    @DisplayName("should find teacher by id")
    public void shouldFindTeacherById() throws Exception{
        // teacher creation
        Teacher teacher = new Teacher();
        teacher.setFirstName("john");
        teacher.setLastName("doe");

        Teacher createdTeacher = teacherRepository.save(teacher);
        Long id = createdTeacher.getId();

        // find teacher by id
        String findRequest = "{ \"id\": \"" + id + "\" }";

        mockMvc.perform(get("/api/teacher/" + id)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(findRequest))
                        .andExpect(status().isOk())
                        .andReturn();
    }

    @Test
    @DisplayName("should find all teachers")
    public void shouldFindTeachers() throws Exception{
        // teacher creation
        Teacher teacher1 = new Teacher();
        teacher1.setFirstName("john");
        teacher1.setLastName("doe");

        teacherRepository.save(teacher1);

        // teacher creation
        Teacher teacher2 = new Teacher();
        teacher2.setFirstName("john");
        teacher2.setLastName("doe");

        teacherRepository.save(teacher2);

        // find teacher by id
        mockMvc.perform(get("/api/teacher/")
                        .header("Authorization", "Bearer " + token))
                        .andExpect(status().isOk())
                        .andReturn();
    }
}
