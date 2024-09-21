package com.openclassrooms.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.openclassrooms.starterjwt.models.Session;

import org.junit.jupiter.api.Tag;
import static org.assertj.core.api.Assertions.assertThat;

@Tag("SessionService")
@DisplayName("unit tests for session service")
@ExtendWith(MockitoExtension.class)
public class SessionServiceTest {
    
    @Mock
    Session session;

    @BeforeEach
    public void setup(){
        // initialiser session
        // h2 bdd ? 
    }

    @Test
    public void createSession(){
    }

    @Test
    public void deleteSession(){
    }

    @Test
    public void findAllSession(){
    }

    @Test
    public void getSessionById(){
    }

    @Test
    public void updateSession(){
    }

    @Test
    public void participateToSession(){
    }

    @Test
    public void noLongerParticipateToSession(){
    }
}
