package com.openclassrooms.starterjwt.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import com.openclassrooms.starterjwt.services.UserService;
import org.springframework.test.context.ActiveProfiles;

@Tag("UserController")
@DisplayName("unit tests for UserController")
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private SecurityContext securityContext;

    @Test
    @DisplayName("should find by id")
    public void shouldFindById(){
        Long id = 1L;

        User user = mock(User.class);

        UserDto userDto = mock(UserDto.class);

        when(userService.findById(id)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userDto);

        ResponseEntity<?> responseEntity = userController.findById(id.toString());

        verify(userService, times(1)).findById(id);
        verify(userMapper, times(1)).toDto(user);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("should find by id because id is incorrect")
    public void shouldNotFindById_1(){
        String id = "oups";

        ResponseEntity<?> responseEntity = userController.findById(id);

        verify(userService, times(0)).findById(anyLong());
        verify(userMapper, times(0)).toDto(anyList());

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("should not find by id because user not found")
    public void shouldNotFindById_2(){
        Long id = 1L;
        
        when(userService.findById(id)).thenReturn(null);

        ResponseEntity<?> responseEntity = userController.findById(id.toString());

        verify(userService, times(1)).findById(id);
        verify(userMapper, times(0)).toDto(anyList());

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("should delete user")
    public void shouldSaveUser(){
        Long id = 1L;
        String email = "toto@gmail.com";
        String firstname = "toto";
        String lastname = "titi";
        boolean isAdmin = false;
        String password = "toto123!";
        User user = new User(id, email, lastname, firstname, password, isAdmin, null, null);
        UserDetailsImpl userDetailsImpl = new UserDetailsImpl(id, email, firstname, lastname, null, password);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetailsImpl, null);
        SecurityContextHolder.setContext(securityContext);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(userService.findById(id)).thenReturn(user);
        doNothing().when(userService).delete(id);

        ResponseEntity<?> response = userController.save(id.toString());

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("should not delete user because user is null")
    public void shouldNotSaveUser_1(){
        Long id = 1L;

        when(userService.findById(id)).thenReturn(null);

        ResponseEntity<?> responseEntity = userController.save(id.toString());

        verify(userService, times(0)).delete(id);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("should not delete user because id is incorrect")
    public void shouldNotSaveUser_2(){
        String id = "oups";

        ResponseEntity<?> responseEntity = userController.save(id);

        verify(userService, times(0)).delete(anyLong());

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("should not delete user because user is not authorized")
    public void shouldNotSaveUser_3(){
        Long id = 1L;
        String email = "toto@gmail.com";
        String firstname = "toto";
        String lastname = "titi";
        boolean isAdmin = false;
        String password = "toto123!";
        User user = new User(id, email, lastname, firstname, password, isAdmin, null, null);
        // En authentification un email différent
        UserDetailsImpl userDetailsImpl = new UserDetailsImpl(id, "differentEmail@gmail.com", firstname,
                        lastname, null, password);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetailsImpl, null);
        SecurityContextHolder.setContext(securityContext);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(userService.findById(id)).thenReturn(user);

        // ACT
        ResponseEntity<?> response = userController.save(id.toString());

        // ASSERT on s'attend à ce que le code retour soit UNAUTHORIZED
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

}
