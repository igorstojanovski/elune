package org.programirame.controller;

import org.programirame.models.Subject;
import org.programirame.services.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/subject")
@ExposesResourceFor(Subject.class)
public class SubjectController {

    private SubjectService subjectService;

    @Autowired
    public SubjectController(SubjectService subjectService) {
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
