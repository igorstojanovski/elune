package org.programirame.controller;


import org.programirame.models.UserApp;
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
@ExposesResourceFor(UserApp.class)
public class UserController {

    private final UserService userService;
    private final EntityLinks entityLinks;

    @Autowired
    public UserController(UserService userService, EntityLinks entityLinks) {
        this.userService = userService;
        this.entityLinks = entityLinks;
    }

    /**
     * Creates a {@link UserApp} and returns the newly created object.
     *
     * @param userApp The new UserApp resource to be created.
     * @return Newly created {@link UserApp} object.
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity registerNewUser(@RequestBody UserApp userApp) {

        Resource<UserApp> resource = new Resource<>(userService.createUser(userApp));
        resource.add(entityLinks.linkToSingleResource(UserApp.class, resource.getContent().getId()));
        return new ResponseEntity<>(resource, HttpStatus.CREATED);
    }

    /**
     * Service for retrieving all the users.
     *
     * @return list of all users.
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Resource<UserApp>>> getAllUsers() {
        List<UserApp> userApps = userService.getAllUsers();
        return new ResponseEntity<>(getUserResources(userApps), HttpStatus.CREATED);
    }

    private Resource<UserApp> getUserResource(UserApp userApp) {
        Resource<UserApp> resource = new Resource<>(userService.createUser(userApp));
        resource.add(entityLinks.linkToSingleResource(UserApp.class, resource.getContent().getId()));

        return resource;
    }

    private List<Resource<UserApp>> getUserResources(List<UserApp> userApps) {

        return userApps.stream().map(this::getUserResource).collect(Collectors.toList());
    }
}
