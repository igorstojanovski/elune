package org.programirame.services;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.programirame.models.User;
import org.programirame.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;

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
        long id = 1;

        userRepository = mock(UserRepository.class);

        userService = new UserService(userRepository);

        userRequested = new User("igor", "stojanovski", "igorce", "igorce");
        userCreated = new User("igor", "stojanovski", "igorce", "igorce");
        userCreated.setId(id);

        when(userRepository.save(userRequested)).thenReturn(userCreated);

        List<User> list = new ArrayList<>();
        list.add(userCreated);
        Iterable iterable = list;

        when(userRepository.findAll()).thenReturn(iterable);
    }

    @Test
    public void shouldCallUserRepositorySaveMethod() {
        User user = userService.createUser(userRequested);

        Mockito.verify(userRepository).save(userRequested);
        assertEquals(user, userCreated);
    }

    @Test
    public void shouldReturnAlistOfUsers() {
        List<User> users = userService.getAllUsers();

        assertEquals(users.size(), 1);
        assertEquals(users.get(0).getId(), 1);
    }
}
