package org.programirame.services;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.programirame.models.UserApp;
import org.programirame.repositories.UserRepository;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserAppServiceTest {

    private UserRepository userRepository;
    private UserService userService;

    private UserApp userAppRequested;
    private UserApp userAppCreated;

    @Before
    public void init() {
        userRepository = mock(UserRepository.class);

        userService = new UserService(userRepository);

        userAppRequested = new UserApp("igor", "stojanovski", "igorce", "igorce");
        userAppCreated = new UserApp("igor", "stojanovski", "igorce", "igorce");
        userAppCreated.setId(1);

        when(userRepository.save(userAppRequested)).thenReturn(userAppCreated);
    }

    @Test
    public void shouldCallUserRepositorySaveMethod() {
        UserApp userApp = userService.createUser(userAppRequested);

        Mockito.verify(userRepository).save(userAppRequested);
        assertEquals(userApp, userAppCreated);
    }


}
