package org.programirame.controller;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.programirame.models.User;
import org.programirame.services.UserService;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    private static final long NEW_USER_ID = 1L;
    private static final String NEW_RESOURCE_URL = "http://localhost/api/user/1";
    private UserService userService;
    private EntityLinks entityLinks;
    private UserController userController;

    private User userRequested;
    private User userCreated;

    @Before
    public void init() {

        userRequested = new User("igor", "stojanovski", "igorce", "igorce");
        userCreated = new User("igor", "stojanovski", "igorce", "igorce");
        userCreated.setId(1L);

        userService = mock(UserService.class);
        entityLinks = mock(EntityLinks.class);

        userController = new UserController(userService, entityLinks);
        when(entityLinks.linkToSingleResource(User.class, NEW_USER_ID)).thenReturn(new Link(NEW_RESOURCE_URL));

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
}
