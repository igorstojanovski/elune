package org.programirame.services;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.programirame.models.User;
import org.programirame.repositories.UserRepository;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserServiceTest {

    private UserRepository userRepository;
    private UserService userService;

    private User userRequested;
    private User userCreated;

    @Before
    public void init() {
        userRepository = mock(UserRepository.class);

        userService = new UserService(userRepository);

        userRequested = new User("igor", "stojanovski", "igorce", "igorce");
        userCreated = new User("igor", "stojanovski", "igorce", "igorce");
        userCreated.setId(1);

        when(userRepository.save(userRequested)).thenReturn(userCreated);
    }

    @Test
    public void shouldCallUserRepositorySaveMethod() {
        User user = userService.createUser(userRequested);

        Mockito.verify(userRepository).save(userRequested);
        assertEquals(user, userCreated);
    }


}
