package org.programirame.services;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.programirame.exceptions.InvalidDataException;
import org.programirame.models.User;
import org.programirame.models.UserTypes;
import org.programirame.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.programirame.services.events.TestingConstants.NAME;
import static org.programirame.services.events.TestingConstants.PASSWORD;
import static org.programirame.services.events.TestingConstants.SURNAME;
import static org.programirame.services.events.TestingConstants.USERNAME;

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

        userRequested = new User(NAME, SURNAME, USERNAME, PASSWORD);
        userCreated = new User(NAME, SURNAME, USERNAME, PASSWORD);
        userCreated.setId(id);

        when(userRepository.save(userRequested)).thenReturn(userCreated);

        List<User> list = new ArrayList<>();
        list.add(userCreated);

        when(userRepository.findAll()).thenReturn((Iterable) list);
    }

    @Test
    public void shouldCallUserRepositorySaveMethod() throws InvalidDataException {
        User user = userService.createUser(userRequested);

        Mockito.verify(userRepository).save(userRequested);
        assertEquals(user, userCreated);
    }

    @Test(expected = InvalidDataException.class)
    public void shouldNotCreateUserIfItHasNoUsername() throws InvalidDataException {
        User user = new User();
        user.setName(NAME);
        userService.createUser(new User());
    }

    @Test(expected = InvalidDataException.class)
    public void shouldNotCreateUserIfItHasNoName() throws InvalidDataException {
        User user = new User();
        user.setPassword(PASSWORD);
        user.setUserType(UserTypes.ADMIN);
        user.setUsername(USERNAME);
        userService.createUser(new User());
    }

    @Test(expected = InvalidDataException.class)
    public void shouldNotCreateUserIfItHasNoPassword() throws InvalidDataException {
        User user = new User();
        user.setPassword(PASSWORD);
        user.setUserType(UserTypes.ADMIN);
        user.setUsername(USERNAME);
        userService.createUser(new User());
    }

    @Test(expected = InvalidDataException.class)
    public void shouldNotCreateUserIfItHasNoUserType() throws InvalidDataException {
        User user = new User();
        user.setName(NAME);
        user.setPassword(PASSWORD);
        user.setUsername(USERNAME);
        userService.createUser(new User());
    }

    @Test
    public void shouldReturnAlistOfUsers() {
        List<User> users = userService.getAllUsers();

        assertEquals(users.size(), 1);
        assertEquals(users.get(0).getId(), 1);
    }
}
