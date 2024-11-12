package com.openclassrooms.starterjwt.services;

import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.test.context.ActiveProfiles;


@Tag("SessionService")
@DisplayName("unit tests for session service")
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class SessionServiceTest {
      
    @InjectMocks
    private SessionService sessionService;

    @Mock
    private SessionRepository sessionRepository;

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Test
    @DisplayName("Should create a session")
    public void shouldCreateSession(){
        // GIVEN

        Session expectedSession = new Session()
        .setId(1L)
        .setName("Session test")
        .setDate(new Date())
        .setDescription("description test")
        .setUsers(null)
        .setCreatedAt(LocalDateTime.now())
        .setUpdatedAt(LocalDateTime.now());

        // WHEN
        when(sessionRepository.save(any(Session.class))).thenReturn(expectedSession);
        
        // THEN
        Session createdSession = sessionService.create(expectedSession);

        verify(sessionRepository, times(1)).save(expectedSession);

        assertThat(createdSession).isNotNull();
        assertThat(createdSession.getId()).isNotNull();
        assertThat(createdSession.getName()).isEqualTo(expectedSession.getName());
        assertThat(createdSession.getDate()).isEqualTo(expectedSession.getDate());
        assertThat(createdSession.getDescription()).isEqualTo(expectedSession.getDescription());
        assertThat(createdSession.getCreatedAt()).isEqualTo(expectedSession.getCreatedAt());
        assertThat(createdSession.getUpdatedAt()).isEqualTo(expectedSession.getUpdatedAt());
    }

    @Test
    @DisplayName("Should not found a session by id")
    public void shouldNotFoundSessionById_ThrowsNullPointerException(){
        // GIVEN 

        // WHEN 
        when(sessionRepository.findById(1L)).thenReturn(null);

        // THEN
        assertThrows(NullPointerException.class, () -> sessionService.getById(1L));

        verify(sessionRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should found a session by id")
    public void shouldFoundSessionById(){
        // GIVEN 
        Session expectedSession = new Session()
                        .setId(1L)
                        .setName("Session")
                        .setDate(new Date())
                        .setDescription("description")
                        .setTeacher(null)
                        .setUsers(null)
                        .setCreatedAt(LocalDateTime.now())
                        .setUpdatedAt(LocalDateTime.now());

        // WHEN
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(expectedSession));

        // THEN
        Session session = sessionService.getById(1L);

        verify(sessionRepository, times(1)).findById(1L);


        assertThat(session).isNotNull();
        assertThat(session.getId()).isNotNull();
        assertThat(session.getId()).isEqualTo(expectedSession.getId());
        assertThat(session.getName()).isEqualTo(expectedSession.getName());
        assertThat(session.getDate()).isEqualTo(expectedSession.getDate());
        assertThat(session.getDescription()).isEqualTo(expectedSession.getDescription());
        assertThat(session.getCreatedAt()).isEqualTo(expectedSession.getCreatedAt());
        assertThat(session.getUpdatedAt()).isEqualTo(expectedSession.getUpdatedAt());
    }

    @Test
    @DisplayName("Should delete a session by id")
    public void shouldDeleteSession(){
        // GIVEN 
        Long sessionId = 1L;

        // WHEN 

        // THEN
        sessionService.delete(sessionId);

        Session found =  sessionService.getById(sessionId);

        assertThat(found).isNull();
    }

    @Test
    @DisplayName("Should find all sessions")
    public void shouldFindSessions(){
        // GIVEN 
        List<Session> list = new ArrayList<>();
        list.add(new Session()
                    .setId(1L)
                    .setName("Session 1"));
        list.add(new Session()  
                    .setId(2L)
                    .setName("Session 2"));
        list.add(new Session()
                    .setId(3L)
                    .setName("Session 3"));
        // WHEN 
        when(sessionRepository.findAll()).thenReturn(list);

        // THEN
        List<Session> found = sessionService.findAll();

        verify(sessionRepository, times(1)).findAll();

        assertThat(found).isNotNull();
        assertThat(found.get(0).getId()).isEqualTo(list.get(0).getId());
        assertThat(found.get(0).getName()).isEqualTo(list.get(0).getName());
        assertThat(found.get(1).getId()).isEqualTo(list.get(1).getId());
        assertThat(found.get(1).getName()).isEqualTo(list.get(1).getName());
        assertThat(found.get(2).getId()).isEqualTo(list.get(2).getId());
        assertThat(found.get(2).getName()).isEqualTo(list.get(2).getName());
    }

    @Test
    @DisplayName("Should delete a session by id")
    public void shouldNotFoundSessions_ThrowsNullPointerException(){
        // GIVEN

        // WHEN
        when(sessionRepository.findAll()).thenReturn(null);

        // THEN
        List<Session> found = sessionService.findAll();

        verify(sessionRepository, times(1)).findAll();

        assertThat(found).isNull();
    }

    @Test
    @DisplayName("Should find a session by id")
    public void shouldFindSessionById(){
        // GIVEN
        Session expectedSession = new Session()
                                .setId(1L)
                                .setName("session")
                                .setDate(new Date())
                                .setDescription("the session")
                                .setCreatedAt(LocalDateTime.now())
                                .setUpdatedAt(LocalDateTime.now());

        // WHEN 
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(expectedSession));

        // THEN
        Session found = sessionService.getById(1L);

        verify(sessionRepository, times(1)).findById(1L);

        assertThat(found).isNotNull();
        assertThat(found.getId()).isEqualTo(expectedSession.getId());
        assertThat(found.getName()).isEqualTo(expectedSession.getName());
        assertThat(found.getDate()).isEqualTo(expectedSession.getDate());
        assertThat(found.getDescription()).isEqualTo(expectedSession.getDescription());
        assertThat(found.getCreatedAt()).isEqualTo(expectedSession.getCreatedAt());
        assertThat(found.getUpdatedAt()).isEqualTo(expectedSession.getUpdatedAt());
    }

    @Test
    @DisplayName("Should update a session")
    public void shouldUpdateSession(){
        // GIVEN
        Session expectedSession = new Session()
        .setId(50L)
        .setName("Session")
        .setDate(new Date())
        .setDescription("description")
        .setCreatedAt(LocalDateTime.now())
        .setUpdatedAt(LocalDateTime.now());

        // WHEN 
        when(sessionRepository.save(any(Session.class))).thenReturn(expectedSession);

        // THEN 
        Session updated = sessionService.update(expectedSession.getId(), expectedSession);

        verify(sessionRepository, times(1)).save(any(Session.class));

        assertThat(updated).isNotNull();
        assertThat(updated.getId()).isNotNull();
        assertThat(updated.getId()).isEqualTo(expectedSession.getId());
        assertThat(updated.getName()).isEqualTo(expectedSession.getName());
        assertThat(updated.getDate()).isEqualTo(expectedSession.getDate());
        assertThat(updated.getDescription()).isEqualTo(expectedSession.getDescription());
        assertThat(updated.getCreatedAt()).isEqualTo(expectedSession.getCreatedAt());
        assertThat(updated.getUpdatedAt()).isEqualTo(expectedSession.getUpdatedAt());
    }
    
    @Test
    @DisplayName("Should participate to session")
    public void shouldParticipateToSession(){
        // GIVEN
        User user = new User()
                .setId(1L)
                .setFirstName("user")
                .setLastName("lastname")
                .setEmail("user@gmail.com");

        List<User> listOfUsers = new ArrayList<>();
        listOfUsers.add(user);

        Session session = new Session()
        .setId(1L)
        .setUsers(new ArrayList<>());

        Session expectedSession = new Session()
        .setId(1L)
        .setUsers(listOfUsers);

        // WHEN 
        when(sessionRepository.findById(session.getId())).thenReturn(Optional.of(session));
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(sessionRepository.save(any(Session.class))).thenReturn(expectedSession);

        // THEN
        sessionService.participate(session.getId(),user.getId());       
        
        verify(sessionRepository, times(1)).findById(session.getId());
        verify(userRepository, times(1)).findById(user.getId());
        verify(sessionRepository, times(1)).save(any(Session.class));
    }
    
    @Test
    @DisplayName("Should not participate to session because session not found")
    public void shouldNotParticipateToSession_1_ThrowsNotFoundException(){
        // GIVEN
        Long sessionId = 1L;
        Long userId = 1L;
        // WHEN 

        // THEN
        assertThrows(NotFoundException.class, () -> sessionService.participate(sessionId,userId));

        verify(sessionRepository, times(1)).findById(sessionId);
        verify(userRepository, times(1)).findById(userId);
        verify(sessionRepository, times(0)).save(any(Session.class));
    }

    @Test
    @DisplayName("Should not participate to session because user not found")
    public void shouldNotParticipateToSession_2_ThrowsNotFoundException(){
        // GIVEN
        Session expectedSession = new Session()
                            .setId(1L)
                            .setName("session")
                            .setDate(new Date())
                            .setDescription("description")
                            .setCreatedAt(LocalDateTime.now())
                            .setUpdatedAt(LocalDateTime.now());
        Long userId = 1L;

        // WHEN 
        when(sessionRepository.findById(expectedSession.getId())).thenReturn(Optional.of(expectedSession));

        // THEN
        Session foundSession = sessionService.getById(expectedSession.getId());
        
        assertThrows(NotFoundException.class, () -> sessionService.participate(expectedSession.getId(),userId));

        verify(sessionRepository, times(2)).findById(expectedSession.getId());
        verify(userRepository, times(1)).findById(userId);
        verify(sessionRepository, times(0)).save(any(Session.class));
        
        assertThat(foundSession).isNotNull();
        assertThat(foundSession.getId()).isEqualTo(expectedSession.getId());
        assertThat(foundSession.getName()).isEqualTo(expectedSession.getName());
        assertThat(foundSession.getDate()).isEqualTo(expectedSession.getDate());
        assertThat(foundSession.getDescription()).isEqualTo(expectedSession.getDescription());
        assertThat(foundSession.getCreatedAt()).isEqualTo(expectedSession.getCreatedAt());
        assertThat(foundSession.getUpdatedAt()).isEqualTo(expectedSession.getUpdatedAt());
    }

    @Test
    @DisplayName("Should not participate to session because user already participate")
    public void shouldNotParticipateToSession_ThrowsBadRequestException(){
        // GIVEN
        User user = new User().setId(25L).setFirstName("user").setLastName("lastname").setEmail("user@gmail.com");
        
        List<User> expectedUser = new ArrayList<User>();
        expectedUser.add(user);

        Session expectedSession = new Session()
        .setId(1L)
        .setUsers(expectedUser);

        // WHEN 
        when(sessionRepository.findById(expectedSession.getId())).thenReturn(Optional.of(expectedSession));
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        // THEN
        assertThrows(BadRequestException.class, () -> sessionService.participate(expectedSession.getId(),user.getId()));

        verify(sessionRepository, times(1)).findById(expectedSession.getId());
        verify(userRepository, times(1)).findById(user.getId());
        verify(sessionRepository, times(0)).save(any(Session.class));
        
    }

    @Test
    @DisplayName("Should not longer participate to session")
    public void shouldNoLongerParticipateToSession(){
        // GIVEN
        User user = new User()
                .setId(25L)
                .setFirstName("user")
                .setLastName("lastname")
                .setEmail("user@gmail.com");

        List<User> listOfUsers = new ArrayList<>();
        listOfUsers.add(user);

        Session session = new Session()
        .setId(1L)
        .setUsers(listOfUsers);

        Session expectedSession = new Session()
                                    .setId(1L)
                                    .setUsers(new ArrayList<>());

        // WHEN 
        when(sessionRepository.findById(session.getId())).thenReturn(Optional.of(session));
        when(sessionRepository.save(any(Session.class))).thenReturn(expectedSession);

        // THEN
        sessionService.noLongerParticipate(session.getId(),user.getId());       
        
        verify(sessionRepository, times(1)).findById(session.getId());
        verify(sessionRepository, times(1)).save(any(Session.class));

    }

    @Test
    @DisplayName("Should throws not found exception because session is null")
    public void shouldNoLongerParticipateToSession_ThrowsNotFoundException(){
         // GIVEN
        Long sessionId = 1L;
        Long userId = 1L;

        // WHEN 

        // THEN
        assertThrows(NotFoundException.class, () -> sessionService.noLongerParticipate(sessionId,userId));       
        
        verify(sessionRepository, times(1)).findById(sessionId);
        verify(sessionRepository, times(0)).save(any(Session.class));
    }

    @Test
    @DisplayName("Should throws BadRequestException because user doesn't participate to session initialy")
    public void shouldNoLongerParticipateToSession_ThrowsBadRequestException(){
        // GIVEN
        Long sessionId = 1L;
        Long userId = 1L;

        Session expectedSession = new Session()
                                    .setId(sessionId)
                                    .setUsers(new ArrayList<>());

        // WHEN 
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(expectedSession));

        // THEN
        assertThrows(BadRequestException.class, () -> sessionService.noLongerParticipate(sessionId,userId));       
        
        verify(sessionRepository, times(1)).findById(sessionId);
        verify(sessionRepository, times(0)).save(any(Session.class));

    }
}
