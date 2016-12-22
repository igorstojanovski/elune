package org.programirame.controller;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.programirame.models.UserApp;
import org.programirame.services.UserService;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserAppControllerTest {

    private static final long NEW_USER_ID = 1L;
    private static final String NEW_RESOURCE_URL = "http://localhost/api/user/1";
    private UserService userService;
    private EntityLinks entityLinks;
    private UserController userController;

    private UserApp userAppRequested;
    private UserApp userAppCreated;

    @Before
    public void init() {

        userAppRequested = new UserApp("igor", "stojanovski", "igorce", "igorce");
        userAppCreated = new UserApp("igor", "stojanovski", "igorce", "igorce");
        userAppCreated.setId(1L);

        userService = mock(UserService.class);
        entityLinks = mock(EntityLinks.class);

        userController = new UserController(userService, entityLinks);
        when(entityLinks.linkToSingleResource(UserApp.class, NEW_USER_ID)).thenReturn(new Link(NEW_RESOURCE_URL));

        when(userService.createUser(userAppRequested)).thenReturn(userAppCreated);
    }

    @Test
    public void shouldCallUserServiceCreateMethod() {
        userController.registerNewUser(userAppRequested);
        Mockito.verify(userService).createUser(userAppRequested);
    }

    @Test
    public void shouldReturn201WhenUserIsCreated() {
        ResponseEntity response = userController.registerNewUser(userAppRequested);

        assertEquals(response.getStatusCodeValue(), HttpStatus.CREATED.value());
    }
}
