package com.openclassrooms.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.Optional;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.UserService;

@Tag("UserService")
@DisplayName("unit tests for user service")
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    
     @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;


    @Test
    @DisplayName("should delete user by id")
    public void shouldDeleteUserById(){
        // GIVEN
        Long userId = Long.valueOf(1);

        // WHEN
        
        // THEN
        userService.delete(userId);

        User found = userService.findById(userId);

        verify(userRepository, times(1)).deleteById(userId);
        verify(userRepository, times(1)).findById(userId);

        assertThat(found).isNull();
    }
    
    @Test
    @DisplayName("should fin user by id")
    public void shouldFindUserById(){
        // GIVEN
        Long userId = Long.valueOf(1);

        User expectedUser = new User()
                            .setId(userId)
                            .setLastName("doe")
                            .setFirstName("john")
                            .setEmail("j.d@gmail.com");

        // WHEN
        when(userRepository.findById(userId)).thenReturn(Optional.of(expectedUser));
        
        // THEN
        User found = userService.findById(userId);

        verify(userRepository, times(1)).findById(userId);

        assertThat(found).isNotNull();
        assertThat(found.getId()).isEqualTo(expectedUser.getId());
        assertThat(found.getLastName()).isEqualTo(expectedUser.getLastName());
        assertThat(found.getFirstName()).isEqualTo(expectedUser.getFirstName());
        assertThat(found.getEmail()).isEqualTo(expectedUser.getEmail());

    }

    @Test
    @DisplayName("should not find user by id")
    public void shouldNotFindUserById(){
        // GIVEN
        Long userId = Long.valueOf(1);

        // WHEN

        
        // THEN
        User found = userService.findById(userId);
        
        verify(userRepository, times(1)).findById(userId);

        assertThat(found).isNull();

    }
}
