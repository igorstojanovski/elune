package org.programirame.services;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.programirame.models.User;
import org.programirame.repositories.UserRepository;

import static org.mockito.Mockito.mock;

public class UserServiceTest {

    UserRepository userRepository;
    UserService userService;

    private User userRequested;

    @Before
    public void init() {
        userRepository = mock(UserRepository.class);
        userService = new UserService(userRepository);
        userRequested = new User("igor", "stojanovski", "igorce", "igorce");
    }

    @Test
    public void shouldCallUserRepositorySaveMethod() {
        userService.createUser(userRequested);

        Mockito.verify(userRepository).save(userRequested);
    }


}
