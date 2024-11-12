package com.openclassrooms.starterjwt.security.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.springframework.test.context.ActiveProfiles;

@Tag("UserDetailsServiceImpl")
@DisplayName("unit tests for UserDetailsServiceImpl")
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class UserDetailsServiceImplTest {
    
    @InjectMocks
    private UserDetailsServiceImpl userDetailsServiceImpl;
    
    @Mock
    private UserRepository userRepository;
    
    @Test
    @DisplayName("should load user by username")
    public void shouldLoadUserByUsername(){
        User user = new User()
                .setId(1L)
                .setEmail("jd@gmail.com")
                .setPassword("superPassword")
                .setFirstName("john")
                .setLastName("doe");

        String username = "jd@gmail.com";

        when(userRepository.findByEmail(username)).thenReturn(Optional.of(user));

        UserDetails found = userDetailsServiceImpl.loadUserByUsername(username);

        verify(userRepository, times(1)).findByEmail(username);

        assertThat(found).isNotNull();
        assertThat(found.getUsername()).isEqualTo(user.getEmail());
        assertThat(found.getPassword()).isEqualTo(user.getPassword());
    }

    @Test
    @DisplayName("should not load user by username")
    public void shouldNotLoadUserByUsername(){
        String username = "jd@gmail.com";

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, 
                                                                        () -> userDetailsServiceImpl.loadUserByUsername(username));

        verify(userRepository, times(1)).findByEmail(username);

        assertThat(exception.getMessage()).isEqualTo("User Not Found with email: " + username);
    }
}
