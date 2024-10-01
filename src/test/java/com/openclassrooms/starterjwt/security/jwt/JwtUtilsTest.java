package com.openclassrooms.starterjwt.security.jwt;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.Date;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Tag("JwtUtils")
@DisplayName("unit tests for JwtUtils")
@ExtendWith(MockitoExtension.class)
public class JwtUtilsTest {
    
    @InjectMocks
    private JwtUtils jwtUtils;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock 
    private UserRepository userRepository;

    private String jwtSecret = "openclassrooms";

    private int jwtExpirationMs = 86400000;

    private User user;

    @BeforeEach
    void setup(){
        User lUser = new User();
        lUser.setFirstName("john");
        lUser.setLastName("doe");
        lUser.setEmail("john.doe@gmail.com");
        lUser.setPassword("supapassword");

        user = userRepository.save(lUser);
    }

    @Test
    @DisplayName("should generate jwt token")
    public void shouldGenerateJwtToken(){

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
        String token = jwtUtils.generateJwtToken(authentication);

        assertThat(token).isNotNull();
    }

    @Test
    @DisplayName("should get username from jwt token")
    public void shouldGetUsernameFromJwtToken(){
        // token creation
        String createdToken = Jwts.builder()
                            .setSubject(user.getEmail())
                            .setIssuedAt(new Date())
                            .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                            .signWith(SignatureAlgorithm.HS512, jwtSecret)
                            .compact();

        // get username from token
        String foundUsername = jwtUtils.getUserNameFromJwtToken(createdToken);

        assertThat(foundUsername).isEqualTo(user.getEmail());
    }
}
