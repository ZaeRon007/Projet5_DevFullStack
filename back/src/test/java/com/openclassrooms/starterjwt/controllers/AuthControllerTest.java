package com.openclassrooms.starterjwt.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;

@Tag("AuthController")
@DisplayName("unit tests for AuthController")
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

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("should register user")
    public void shouldRegisterUser(){
        SignupRequest signupRequest = new SignupRequest();
        
        signupRequest.setFirstName("john");
        signupRequest.setEmail("j.d@gmail.com");
        signupRequest.setLastName("doe");
        signupRequest.setPassword("superpassword1234");

        User expectedUser = new User()
        .setEmail(signupRequest.getEmail())
        .setFirstName(signupRequest.getFirstName())
        .setLastName(signupRequest.getLastName())
        .setPassword(signupRequest.getPassword())
        .setAdmin(false);

        when(userRepository.existsByEmail(signupRequest.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("superencryptedpassword");
        when(userRepository.save(any(User.class))).thenReturn(expectedUser);

        ResponseEntity<?> responseEntity = authController.registerUser(signupRequest);

        verify(userRepository, times(1)).existsByEmail(anyString());
        verify(passwordEncoder, times(1)).encode(anyString());
        verify(userRepository,times(1)).save(any(User.class));

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    
    @Test
    @DisplayName("should not register user because email already exist")
    public void shouldNotRegisterUser_ThrowBadRequest(){
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("j.d@gmail.com");

        when(userRepository.existsByEmail(signupRequest.getEmail())).thenReturn(true);

        ResponseEntity<?> responseEntity = authController.registerUser(signupRequest);

        verify(userRepository, times(1)).existsByEmail(anyString());
        verify(passwordEncoder, times(0)).encode(anyString());
        verify(userRepository,times(0)).save(any(User.class));

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
