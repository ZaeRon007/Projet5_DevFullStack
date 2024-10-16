package com.openclassrooms.starterjwt.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import java.util.ArrayList;
import java.util.List;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.services.SessionService;

@Tag("SessionController")
@DisplayName("unit tests for SessionController")
@ExtendWith(MockitoExtension.class)
public class SessionControllerTest {

    @InjectMocks
    private SessionController sessionController;

    @Mock
    private SessionService sessionService;

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private SessionMapper sessionMapper;


    @Test
    @DisplayName("should find a session by id")
    public void shouldFindById(){
        Long id = 1L;

        Session expectedSession = new Session()
        .setId(1L)
        .setName("Session test")
        .setDescription("description test");

        SessionDto sessionDto = mock(SessionDto.class);

        when(sessionService.getById(id)).thenReturn(expectedSession);
        when(sessionMapper.toDto(any(Session.class))).thenReturn(sessionDto);

        ResponseEntity<?> responseEntity = sessionController.findById(id.toString());

        verify(sessionService, times(1)).getById(anyLong());
        verify(sessionMapper, times(1)).toDto(any(Session.class));

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("should not find a session by id because session is null")
    public void shouldNotFindById_1(){
        Long id = 1L;

        when(sessionService.getById(id)).thenReturn(null);

        ResponseEntity<?> responseEntity = sessionController.findById(id.toString());

        verify(sessionService, times(1)).getById(anyLong());
        verify(sessionMapper, times(0)).toDto(any(Session.class));

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("should not find a session by id because id is incorrect")
    public void shouldNotFindById_2(){
        String id = "oups";

        ResponseEntity<?> responseEntity = sessionController.findById(id.toString());

        verify(sessionService, times(0)).getById(anyLong());
        verify(sessionMapper, times(0)).toDto(any(Session.class));

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }


    @Test
    @DisplayName("should find all sessions")
    public void shouldFindAllSessions(){
        Session session = mock(Session.class);
        SessionDto sessionDto = mock(SessionDto.class);

        List<Session> listOfSessions = new ArrayList<>();
        listOfSessions.add(session);

        List<SessionDto> listOfSessionDtos = new ArrayList<>();
        listOfSessionDtos.add(sessionDto);

        when(sessionService.findAll()).thenReturn(listOfSessions);
        when(sessionMapper.toDto(listOfSessions)).thenReturn(listOfSessionDtos);

        ResponseEntity<?> responseEntity = sessionController.findAll();

        verify(sessionService, times(1)).findAll();
        verify(sessionMapper, times(1)).toDto(listOfSessions);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("should create a session")
    public void shouldCreateSession(){
        SessionDto sessionDto = mock(SessionDto.class);
        Session session = mock(Session.class);

        when(sessionMapper.toEntity(sessionDto)).thenReturn(session);
        when(sessionService.create(session)).thenReturn(session);

        ResponseEntity<?> responseEntity = sessionController.create(sessionDto);

        verify(sessionMapper, times(1)).toEntity(sessionDto);
        verify(sessionService, times(1)).create(session);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

    }

    @Test
    @DisplayName("should update session")
    public void shouldUpdateSession(){
        Long id = 1L;
        SessionDto sessionDto = mock(SessionDto.class);
        Session session = mock(Session.class);

        when(sessionMapper.toEntity(sessionDto)).thenReturn(session);
        when(sessionService.update(id, session)).thenReturn(session);

        ResponseEntity<?> responseEntity = sessionController.update(id.toString(), sessionDto);

        verify(sessionMapper, times(1)).toEntity(sessionDto);
        verify(sessionService, times(1)).update(id, session);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("should not update session because id is incorrect")
    public void shouldNotUpdateSession(){
        String id = "oups";

        SessionDto sessionDto = mock(SessionDto.class);
        Session session = mock(Session.class);

        ResponseEntity<?> responseEntity = sessionController.update(id, sessionDto);

        verify(sessionMapper, times(0)).toEntity(sessionDto);
        verify(sessionMapper, times(0)).toDto(session);
        verify(sessionService, times(0)).update(anyLong(), any(Session.class));

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("should save a session")
    public void shouldSaveSession(){
        Long id = 1L;
        Session session = mock(Session.class);

        when(sessionService.getById(id)).thenReturn(session);

        ResponseEntity<?> responseEntity = sessionController.save(id.toString());

        verify(sessionService, times(1)).getById(id);
        verify(sessionService, times(1)).delete(id);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("should not save a session because id is incorrect")
    public void shouldNotSaveSession_1(){
        String id = "oups";

        ResponseEntity<?> responseEntity = sessionController.save(id.toString());

        verify(sessionService, times(0)).getById(anyLong());
        verify(sessionService, times(0)).delete(anyLong());

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("should save a session because session not found")
    public void shouldNotSaveSession_2(){
        Long id = 1L;

        when(sessionService.getById(id)).thenReturn(null);

        ResponseEntity<?> responseEntity = sessionController.save(id.toString());

        verify(sessionService, times(1)).getById(id);
        verify(sessionService, times(0)).delete(id);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("should participate to session")
    public void shouldParticipateToSession(){
        String id = "1";
        String userId = "1";

        ResponseEntity<?> responseEntity = sessionController.participate(id, userId);

        verify(sessionService, times(1)).participate(anyLong(), anyLong());

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("should not participate to session")
    public void shouldNotParticipateToSession(){
        String id = "oups";
        String userId = "oups";

        ResponseEntity<?> responseEntity = sessionController.participate(id, userId);

        verify(sessionService, times(0)).participate(anyLong(), anyLong());

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("should no longer participate to session")
    public void shouldNoLongerParticipate(){
        String id = "1";
        String userId = "1";

        ResponseEntity<?> responseEntity = sessionController.noLongerParticipate(id, userId);

        verify(sessionService, times(1)).noLongerParticipate(anyLong(), anyLong());

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("should not no longer participate to session")
    public void shouldNotNoLongerParticipate(){
        String id = "oups";
        String userId = "oups";

        ResponseEntity<?> responseEntity = sessionController.noLongerParticipate(id, userId);

        verify(sessionService, times(0)).noLongerParticipate(anyLong(), anyLong());

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

}
