package org.programirame.controller;

import org.programirame.models.OfferedService;
import org.programirame.services.OfferedServiceService;
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

@RestController
@RequestMapping("/api/service")
@ExposesResourceFor(OfferedService.class)
public class ServiceController {


    private final EntityLinks entityLinks;
    private final OfferedServiceService offeredServiceService;

    @Autowired
    public ServiceController(OfferedServiceService offeredServiceService, EntityLinks entityLinks) {
        this.offeredServiceService = offeredServiceService;
        this.entityLinks = entityLinks;
    }


    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity addNewService(@RequestBody OfferedService offeredService) {

        Resource<OfferedService> resource = new Resource<>(offeredServiceService.createService(offeredService));
        resource.add(entityLinks.linkToSingleResource(OfferedService.class, resource.getContent().getId()));
        return new ResponseEntity<>(resource, HttpStatus.CREATED);
    }
}
