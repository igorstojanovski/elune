package org.programirame.services;

import org.programirame.models.User;
import org.programirame.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private UserRepository userRepository;

    UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Creates a new user in the database.
     *
     * @param user the user to be created.
     * @return the newly created user with ID set.
     */
    public User createUser(User user) {
        return userRepository.save(user);
    }

    /**
     * Retrieves all the users from the database.
     *
     * @return list of all the users.
     */
    public List<User> getAllUsers() {
        return iterableToList(userRepository.findAll());
    }

    private List<User> iterableToList(Iterable<User> iterable) {
        List<User> users = new ArrayList<>();
        iterable.forEach(users::add);

        return users;
    }
}
