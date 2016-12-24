package org.programirame.controller;


import org.programirame.models.User;
import org.programirame.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
@ExposesResourceFor(User.class)
public class UserController {

    private final UserService userService;
    private final EntityLinks entityLinks;

    @Autowired
    public UserController(UserService userService, EntityLinks entityLinks) {
        this.userService = userService;
        this.entityLinks = entityLinks;
    }

    /**
     * Creates a {@link User} and returns the newly created object.
     *
     * @param user The new User resource to be created.
     * @return Newly created {@link User} object.
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity registerNewUser(@RequestBody User user) {

        Resource<User> resource = new Resource<>(userService.createUser(user));
        resource.add(entityLinks.linkToSingleResource(User.class, resource.getContent().getId()));
        return new ResponseEntity<>(resource, HttpStatus.CREATED);
    }

    /**
     * Service for retrieving all the users.
     *
     * @return list of all users.
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Resource<User>>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return new ResponseEntity<>(getUserResources(users),
                users.size() > 0 ? HttpStatus.OK : HttpStatus.NOT_FOUND
        );
    }

    private Resource<User> getUserResource(User user) {
        Resource<User> resource = new Resource<>(user);
        resource.add(entityLinks.linkToSingleResource(User.class, resource.getContent().getId()));

        return resource;
    }

    private List<Resource<User>> getUserResources(List<User> users) {

        return users.stream().map(this::getUserResource).collect(Collectors.toList());
    }
}
