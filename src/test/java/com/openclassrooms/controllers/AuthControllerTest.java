package com.openclassrooms.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import com.openclassrooms.starterjwt.controllers.AuthController;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;

@Tag("AuthController")
@DisplayName("unit tests for AutheController")
@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Authentication authentication;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserDetailsImpl userDetailsImpl;

    @Test
    @DisplayName("should authenticate user")
    public void shouldAuthenticateUser(){

        // GIVEN
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("john.doe@gmail.com");
        loginRequest.setPassword("super-password");

        User user = new User();
        user.setAdmin(true);
        
        // WHEN

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
        .thenReturn(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        when(authentication.getPrincipal()).thenReturn(loginRequest.getEmail());

        when(jwtUtils.generateJwtToken(authentication)).thenReturn("this is a fake token");

        when(userRepository.findByEmail(userDetailsImpl.getUsername())).thenReturn(Optional.of(user));

        // THEN

        ResponseEntity<?> responseEntity = authController.authenticateUser(loginRequest);

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtUtils, times(1)).generateJwtToken(authentication);
        verify(userRepository, times(1)).findByEmail(userDetailsImpl.getUsername());

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);        
    }

    @Test
    @DisplayName("should authenticate user because TODEFINE")
    public void shouldNotAuthenticateUser_ThrowsTODEFINE(){
        
    }
    
}
