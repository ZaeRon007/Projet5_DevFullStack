package com.openclassrooms.starterjwt.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.TeacherService;

@Tag("TeacherController")
@DisplayName("unit tests for TeacherController")
@ExtendWith(MockitoExtension.class)
public class TeacherControllerTest {
    
    @InjectMocks
    private TeacherController teacherController;

    @Mock
    private TeacherService teacherService;

    @Mock
    private TeacherMapper teacherMapper;

    @Test
    @DisplayName("should find by id")
    public void shouldFindById(){
        String id = "1";
        Teacher teacher = mock(Teacher.class);
        
        when(teacherService.findById(Long.parseLong(id))).thenReturn(teacher);

        ResponseEntity<?> responseEntity = teacherController.findById(id);

        verify(teacherService, times(1)).findById(anyLong());
        verify(teacherMapper, times(1)).toDto(teacher);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("should not find by id because teacher is null")
    public void shouldNotFindById_1(){
        String id = "1";
        
        when(teacherService.findById(Long.parseLong(id))).thenReturn(null);

        ResponseEntity<?> responseEntity = teacherController.findById(id);

        verify(teacherService, times(1)).findById(anyLong());
        verify(teacherMapper, times(0)).toDto(any(Teacher.class));

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("should not find by id because id is incorrect")
    public void shouldNotFindById_2(){
        String id = "oups";
        
        ResponseEntity<?> responseEntity = teacherController.findById(id);

        verify(teacherService, times(0)).findById(anyLong());
        verify(teacherMapper, times(0)).toDto(any(Teacher.class));

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("should find all teachers")
    public void shouldFindAllTeachers(){
        Teacher teacher = mock(Teacher.class);

        List<Teacher> listOfTeacher = new ArrayList<>();
        listOfTeacher.add(teacher);

        TeacherDto teacherDto = mock(TeacherDto.class);

        List<TeacherDto> listOfTeacherDto = new ArrayList<>();
        listOfTeacherDto.add(teacherDto);

        when(teacherService.findAll()).thenReturn(listOfTeacher);
        when(teacherMapper.toDto(listOfTeacher)).thenReturn(listOfTeacherDto);

        ResponseEntity<?> responseEntity = teacherController.findAll();

        verify(teacherService, times(1)).findAll();
        verify(teacherMapper, times(1)).toDto(listOfTeacher);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
