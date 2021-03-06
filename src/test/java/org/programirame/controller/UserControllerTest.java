package org.programirame.controller;

import org.junit.Before;
import org.junit.Test;
import org.programirame.exceptions.InvalidDataException;
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
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.programirame.services.events.TestingConstants.NAME;
import static org.programirame.services.events.TestingConstants.PASSWORD;
import static org.programirame.services.events.TestingConstants.SURNAME;
import static org.programirame.services.events.TestingConstants.USERNAME;

public class UserControllerTest {

    private static final long NEW_USER_ID = 1L;
    private static final String NEW_RESOURCE_URL = "http://localhost/api/user/1";

    private UserService userService;
    private UserController userController;

    private User userRequested;

    @Before
    public void init() throws InvalidDataException {

        userRequested = new User(NAME, SURNAME, USERNAME, PASSWORD);
        User userCreated = new User(NAME, SURNAME, USERNAME, PASSWORD);
        userCreated.setId(1L);

        userService = mock(UserService.class);
        EntityLinks entityLinks = mock(EntityLinks.class);

        userController = new UserController(userService);
        when(entityLinks.linkToSingleResource(User.class, NEW_USER_ID)).thenReturn(new Link(NEW_RESOURCE_URL));

        List<User> list = new ArrayList<>();
        list.add(userCreated);

        when(userService.getAllUsers()).thenReturn(list);
        doReturn(userCreated).when(userService).createUser(userRequested);
    }

    @Test
    public void shouldCallUserServiceCreateMethod() throws InvalidDataException {
        ResponseEntity<User> result = userController.registerNewUser(userRequested);
        verify(userService).createUser(userRequested);
        assertNotNull(result);
    }

    @Test
    public void shouldReturn201WhenUserIsCreated() throws InvalidDataException {
        ResponseEntity response = userController.registerNewUser(userRequested);

        assertEquals(response.getStatusCodeValue(), HttpStatus.CREATED.value());
    }

    @Test
    public void shouldCallUserServiceToGetAllUsers() {
        ResponseEntity result = userController.getAllUsers();

        verify(userService, times(1)).getAllUsers();
        assertNotNull(result);
    }

    @Test
    public void shouldReturnOKWhenUserFound() {
        ResponseEntity<List<Resource<User>>> usersResponse = userController.getAllUsers();

        assertEquals(usersResponse.getStatusCodeValue(), HttpStatus.OK.value());
    }

    @Test
    public void shouldReturnTheCorrectUser() {
        ResponseEntity<List<User>> usersResponse = userController.getAllUsers();

        assertEquals(usersResponse.getBody().get(0).getId(), 1);
    }
}
