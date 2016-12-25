package org.programirame.controller;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.programirame.models.User;
import org.programirame.services.UserService;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    private static final long NEW_USER_ID = 1L;
    private static final String NEW_RESOURCE_URL = "http://localhost/api/user/1";
    private UserService userService;
    private UserController userController;

    private User userRequested;

    @Before
    public void init() {

        userRequested = new User("igor", "stojanovski", "igorce", "igorce");
        User userCreated = new User("igor", "stojanovski", "igorce", "igorce");
        userCreated.setId(1L);

        userService = mock(UserService.class);
        EntityLinks entityLinks = mock(EntityLinks.class);

        userController = new UserController(userService, entityLinks);
        when(entityLinks.linkToSingleResource(User.class, NEW_USER_ID)).thenReturn(new Link(NEW_RESOURCE_URL));

        List<User> list = new ArrayList<>();
        list.add(userCreated);

        when(userService.getAllUsers()).thenReturn(list);
        when(userService.createUser(userRequested)).thenReturn(userCreated);
    }

    @Test
    public void shouldCallUserServiceCreateMethod() {
        userController.registerNewUser(userRequested);
        Mockito.verify(userService).createUser(userRequested);
    }

    @Test
    public void shouldReturn201WhenUserIsCreated() {
        ResponseEntity response = userController.registerNewUser(userRequested);

        assertEquals(response.getStatusCodeValue(), HttpStatus.CREATED.value());
    }

    @Test
    public void shouldCallUserServiceToGetAllUsers() {
        userController.getAllUsers();

        verify(userService, times(1)).getAllUsers();
    }

    @Test
    public void shouldReturnOKWhenUserFound() {
        ResponseEntity<List<Resource<User>>> usersResponse = userController.getAllUsers();

        assertEquals(usersResponse.getStatusCodeValue(), HttpStatus.OK.value());
    }

    @Test
    public void shouldReturnTheCorrectUser() {
        ResponseEntity<List<Resource<User>>> usersResponse = userController.getAllUsers();

        assertEquals(usersResponse.getBody().get(0).getContent().getId(), 1);
    }
}
