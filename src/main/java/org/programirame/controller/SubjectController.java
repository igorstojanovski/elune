package org.programirame.controller;

import org.programirame.models.Subject;
import org.programirame.services.SubjectService;
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
@RequestMapping("/api/subject")
@ExposesResourceFor(Subject.class)
public class SubjectController {

    private EntityLinks entityLinks;
    private SubjectService subjectService;

    @Autowired
    public SubjectController(EntityLinks entityLinks, SubjectService subjectService) {
        this.entityLinks = entityLinks;
        this.subjectService = subjectService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity createOrganizationalResource(@RequestBody Subject subject) {

        Resource<Subject> resource = new Resource<>(subjectService.createResource(subject));
        resource.add(entityLinks.linkToSingleResource(Subject.class, resource.getContent().getId()));
        return new ResponseEntity<>(resource, HttpStatus.CREATED);
    }
}
