package org.programirame.controller;

import org.programirame.models.Subject;
import org.programirame.services.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Subject> createOrganizationalResource(@RequestBody Subject subject) {

        Subject createdSubject = subjectService.createResource(subject);
        return new ResponseEntity<>(createdSubject, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{subjectId}", method = RequestMethod.GET)
    public ResponseEntity<Subject> getSubject(@PathVariable Long subjectId) {
        Subject subject = subjectService.getSubject(subjectId);
        return new ResponseEntity<>(subject, subject != null ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }
}
