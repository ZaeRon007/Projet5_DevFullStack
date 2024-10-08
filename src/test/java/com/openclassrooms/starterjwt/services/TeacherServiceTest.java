package com.openclassrooms.starterjwt.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import org.springframework.test.context.ActiveProfiles;

@Tag("TeacherService")
@DisplayName("unit tests for teacher service")
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class TeacherServiceTest {
    
    @InjectMocks
    private TeacherService teacherService;

    @Mock
    private TeacherRepository teacherRepository;


    @Test
    @DisplayName("should find all teachers")
    public void shouldFindAllTeachers(){
        // GIVEN
        Teacher professeurTournesol = new Teacher()
                                        .setId(1L)
                                        .setFirstName("Yves")
                                        .setLastName("De La Passion");

        Teacher pereDucrass = new Teacher()
                                        .setId(2L)
                                        .setFirstName("Le père")
                                        .setLastName("Ducrass");

        List<Teacher> listOfTeachers = new ArrayList<>();
        listOfTeachers.add(professeurTournesol);
        listOfTeachers.add(pereDucrass);

        // WHEN
        when(teacherRepository.findAll()).thenReturn(listOfTeachers);

        // THEN
        List<Teacher> found = teacherService.findAll();

        verify(teacherRepository, times(1)).findAll();

        assertThat(found).isNotNull();
        assertThat(found.get(0).getId()).isEqualTo(listOfTeachers.get(0).getId());
        assertThat(found.get(0).getFirstName()).isEqualTo(listOfTeachers.get(0).getFirstName());
        assertThat(found.get(0).getLastName()).isEqualTo(listOfTeachers.get(0).getLastName());
        assertThat(found.get(1).getId()).isEqualTo(listOfTeachers.get(1).getId());
        assertThat(found.get(1).getFirstName()).isEqualTo(listOfTeachers.get(1).getFirstName());
        assertThat(found.get(1).getLastName()).isEqualTo(listOfTeachers.get(1).getLastName());

    }

    @Test
    @DisplayName("should not find teacher")
    public void shouldNotFindTeacher(){
        // GIVEN
        
        // WHEN
        when(teacherRepository.findAll()).thenReturn(new ArrayList<>());

        // THEN
        List<Teacher> found = teacherService.findAll();

        verify(teacherRepository, times(1)).findAll();

        assertThat(found).isEmpty();
    }


    @Test
    @DisplayName("should find teacher by id")
    public void shouldFindTeacherById(){
        // GIVEN
        Long teacherId = 1L;

        Teacher professeurTournesol = new Teacher()
                                        .setId(teacherId)
                                        .setFirstName("Yves")
                                        .setLastName("De La Passion");

        // WHEN
        when(teacherRepository.findById(teacherId)).thenReturn(Optional.of(professeurTournesol));

        // THEN
        Teacher found = teacherService.findById(teacherId);

        verify(teacherRepository, times(1)).findById(teacherId);

        assertThat(found).isNotNull();
        assertThat(found.getId()).isEqualTo(professeurTournesol.getId());
        assertThat(found.getFirstName()).isEqualTo(professeurTournesol.getFirstName());
        assertThat(found.getLastName()).isEqualTo(professeurTournesol.getLastName());

    }

    @Test
    @DisplayName("should not find teacher")
    public void shouldNotFindTeacherById(){
        // GIVEN
        Long teacherId = 1L;

        // WHEN
        when(teacherRepository.findById(teacherId)).thenReturn(Optional.empty());

        // THEN
        Teacher found = teacherService.findById(teacherId);

        verify(teacherRepository, times(1)).findById(teacherId);

        assertThat(found).isNull();
    }
    
}
