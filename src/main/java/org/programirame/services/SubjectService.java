package org.programirame.services;

import org.programirame.models.Subject;
import org.programirame.repositories.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubjectService {

    private final SubjectRepository subjectRepository;

    @Autowired
    public SubjectService(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    public Subject createResource(Subject subject) {
        return subjectRepository.save(subject);
    }

    public Subject getSubject(long id) {
        return subjectRepository.findOne(id);
    }
}
