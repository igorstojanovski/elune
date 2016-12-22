package org.programirame.services;

import org.programirame.models.UserApp;
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
     * Creates a new userApp in the database.
     *
     * @param userApp the userApp to be created.
     * @return the newly created userApp with ID set.
     */
    public UserApp createUser(UserApp userApp) {
        return userRepository.save(userApp);
    }

    /**
     * Retrieves all the users from the database.
     *
     * @return list of all the users.
     */
    public List<UserApp> getAllUsers() {
        return iterableToList(userRepository.findAll());
    }

    private List<UserApp> iterableToList(Iterable<UserApp> iterable) {
        List<UserApp> userApps = new ArrayList<>();
        iterable.forEach(userApps::add);

        return userApps;
    }
}
