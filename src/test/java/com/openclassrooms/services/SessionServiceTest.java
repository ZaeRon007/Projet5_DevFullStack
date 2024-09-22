package com.openclassrooms.services;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.Tag;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
// import javax.persistence.EntityNotFoundException;


@Tag("SessionService")
@DisplayName("unit tests for session service")
@ExtendWith(MockitoExtension.class)
public class SessionServiceTest {
    
    @Mock 
    Teacher teacher;// null pour le moment 

    @Mock
    User user;//null pour le moment
    
    @InjectMocks
    private SessionService sessionService;

    // @BeforeEach
    // @Sql({"/script.sql"})
    // public void setup(){
    //     // initialiser session
    // }
/*
    @Test
    public void shouldNotFoundSessionById_ThrowsEntityNotFoundException(){
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> sessionService.getById(Long.valueOf(0)));

        assertThat(EntityNotFoundException.class).isEqualTo(exception);
    }

    @Test
    public void shouldCreateSession(){
        Session expectedSession = new Session()
        .setName("Session test")
        .setDate(new Date())
        .setDescription("description test")
        .setTeacher(new Teacher())
        .setUsers(null)
        .setCreatedAt(LocalDateTime.now())
        .setUpdatedAt(LocalDateTime.now());

        Session createdSession = sessionService.create(expectedSession);

        assertThat(createdSession).isNotNull();
        assertThat(createdSession.getId()).isNotNull();
        assertThat(createdSession.getName()).isEqualTo(expectedSession.getName());
        assertThat(createdSession.getDate()).isEqualTo(expectedSession.getDate());
        assertThat(createdSession.getDescription()).isEqualTo(expectedSession.getDescription());
        assertThat(createdSession.getTeacher()).isEqualTo(expectedSession.getTeacher());
        assertThat(createdSession.getCreatedAt()).isEqualTo(expectedSession.getCreatedAt());
        assertThat(createdSession.getUpdatedAt()).isEqualTo(expectedSession.getUpdatedAt());
    }

    @Test
    public void shouldDeleteSession_ThrowsNotFoundException(){
        Session expectedSession = new Session()
        .setName("Session to delete")
        .setDate(new Date())
        .setDescription("description to delete")
        .setTeacher(new Teacher())
        .setUsers(null)
        .setCreatedAt(LocalDateTime.now())
        .setUpdatedAt(LocalDateTime.now());

        Session createdSession = sessionService.create(expectedSession);
        Long id = createdSession.getId();

        sessionService.delete(id);

        NotFoundException exception = assertThrows(NotFoundException.class, () -> sessionService.getById(id));

        assertThat(exception).isInstanceOf(NotFoundException.class);
    }

    @Test
    public void shouldFindSessions(){
        List<Session> found = sessionService.findAll();

        assertThat(found).isNotNull();
    }

    @BeforeAll
    public void shouldNotFoundSessions_ThrowsNullPointerException(){
        NullPointerException exception = assertThrows(NullPointerException.class, () -> sessionService.findAll());

        assertThat(exception).isInstanceOf(NullPointerException.class);
    }

    @Test
    public void shouldFindSessionById(){
        Session found = sessionService.getById(Long.valueOf(1));

        assertThat(found).isNotNull();
        assertThat(found.getId()).isEqualTo(1);
    }

    @Test
    public void shouldUpdateSession(){
        Session expectedSession = new Session()
        .setName("Session to update")
        .setDate(new Date())
        .setDescription("description to update")
        .setTeacher(new Teacher())
        .setUsers(null)
        .setCreatedAt(LocalDateTime.now())
        .setUpdatedAt(LocalDateTime.now());

        Session savedSession = sessionService.create(expectedSession);
        Session sessionToUpdate = savedSession;

        sessionToUpdate.setDescription("description updated");

        savedSession = sessionService.update(sessionToUpdate.getId(), sessionToUpdate);

        assertThat(savedSession).isNotNull();
        assertThat(savedSession.getId()).isNotNull();
        assertThat(savedSession.getName()).isEqualTo(sessionToUpdate.getName());
        assertThat(savedSession.getDate()).isEqualTo(sessionToUpdate.getDate());
        assertThat(savedSession.getDescription()).isNotEqualTo(sessionToUpdate.getDescription());
        assertThat(savedSession.getTeacher()).isEqualTo(sessionToUpdate.getTeacher());
        assertThat(savedSession.getCreatedAt()).isEqualTo(sessionToUpdate.getCreatedAt());
        assertThat(savedSession.getUpdatedAt()).isEqualTo(sessionToUpdate.getUpdatedAt());
    }
    
    @Test
    public void shouldParticipateToSession(){
        User expectUser = new User()
        .setId(Long.valueOf(95))
        .setFirstName("John")
        .setLastName("Doe")
        .setEmail("john.doe@gmail.com")
        .setCreatedAt(LocalDateTime.now())
        .setUpdatedAt(LocalDateTime.now())
        .setPassword("test!31")
        .setAdmin(false);

        Session expectedSession = new Session()
        .setName("Session")
        .setDate(new Date())
        .setDescription("description")
        .setTeacher(new Teacher())
        .setUsers(null)
        .setCreatedAt(LocalDateTime.now())
        .setUpdatedAt(LocalDateTime.now());

        Session savedSession = sessionService.create(expectedSession);
        
        sessionService.participate(savedSession.getId(), expectUser.getId());

        Session updatedSession = sessionService.getById(expectedSession.getId());

        assertThat(updatedSession).isNotNull();
        assertThat(updatedSession.getUsers()).contains(expectUser);
    }

    @Test
    public void shouldNotParticipateToSession_ThrowsNotFoundException(){
        NotFoundException exception = assertThrows(NotFoundException.class, () -> sessionService.getById(Long.valueOf(0)));

        assertThat(exception).isInstanceOf(NotFoundException.class);

    }

    @Test
    public void shouldNotParticipateToSession_ThrowsBadRequestException(){
        User expectUser = new User()
        .setId(Long.valueOf(95))
        .setFirstName("John")
        .setLastName("Doe")
        .setEmail("john.doe@gmail.com")
        .setCreatedAt(LocalDateTime.now())
        .setUpdatedAt(LocalDateTime.now())
        .setPassword("test!31")
        .setAdmin(false);

        List<User> list = new ArrayList<>();
        list.add(expectUser);

        Session expectedSession = new Session()
        .setName("Session")
        .setDate(new Date())
        .setDescription("description")
        .setTeacher(new Teacher())
        .setUsers(null)
        .setCreatedAt(LocalDateTime.now())
        .setUpdatedAt(LocalDateTime.now());

        Session savedSession = sessionService.create(expectedSession);
        
        BadRequestException exception = assertThrows(BadRequestException.class, () -> sessionService.participate(savedSession.getId(), expectUser.getId()));

        assertThat(exception).isInstanceOf(BadRequestException.class);

    }

    @Test
    public void shouldNoLongerParticipateToSession(){
        User expectUser = new User()
        .setId(Long.valueOf(95))
        .setFirstName("John")
        .setLastName("Doe")
        .setEmail("john.doe@gmail.com")
        .setCreatedAt(LocalDateTime.now())
        .setUpdatedAt(LocalDateTime.now())
        .setPassword("test!31")
        .setAdmin(false);

        List<User> list = new ArrayList<>();
        list.add(expectUser);

        Session expectedSession = new Session()
        .setName("Session")
        .setDate(new Date())
        .setDescription("description")
        .setTeacher(new Teacher())
        .setUsers(null)
        .setCreatedAt(LocalDateTime.now())
        .setUpdatedAt(LocalDateTime.now());

        Session savedSession = sessionService.create(expectedSession);

        sessionService.noLongerParticipate(savedSession.getId(), expectUser.getId());

        Session updatedSession = sessionService.getById(expectedSession.getId());

        assertThat(updatedSession).isNotNull();
        assertThat(updatedSession.getUsers()).doesNotContain(expectUser);

    }

    @Test
    public void shouldNoLongerParticipateToSessionThrowsNotFoundException(){
        NotFoundException exception = assertThrows(NotFoundException.class, () -> sessionService.noLongerParticipate(
            Long.valueOf(0),
            Long.valueOf(0)));

        assertThat(exception).isInstanceOf(NotFoundException.class);

    }

    @Test
    public void shouldNoLongerParticipateToSession_ThrowsBadRequestException(){
        User expectUser = new User()
        .setId(Long.valueOf(95))
        .setFirstName("John")
        .setLastName("Doe")
        .setEmail("john.doe@gmail.com")
        .setCreatedAt(LocalDateTime.now())
        .setUpdatedAt(LocalDateTime.now())
        .setPassword("test!31")
        .setAdmin(false);

        Session expectedSession = new Session()
        .setName("Session")
        .setDate(new Date())
        .setDescription("description")
        .setTeacher(new Teacher())
        .setUsers(null)
        .setCreatedAt(LocalDateTime.now())
        .setUpdatedAt(LocalDateTime.now());

        Session savedSession = sessionService.create(expectedSession);
        
        BadRequestException exception = assertThrows(BadRequestException.class, () -> sessionService.noLongerParticipate(savedSession.getId(), expectUser.getId()));

        assertThat(exception).isInstanceOf(BadRequestException.class);
    }

*/
}
