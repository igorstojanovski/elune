package org.programirame.services;


import org.junit.Before;
import org.junit.Test;
import org.programirame.models.Subject;
import org.programirame.repositories.SubjectRepository;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SubjectServiceTest {

    private SubjectRepository subjectRepository;
    private SubjectService subjectService;
    private Subject subject;
    private Subject createdSubject;

    @Before
    public void setUp() {
        subjectRepository = mock(SubjectRepository.class);

        subjectService = new SubjectService(subjectRepository);

        subject = getSubject();
        createdSubject = getSubject();
        createdSubject.setId(1L);

        when(subjectRepository.save(subject)).thenReturn(createdSubject);
    }

    private Subject getSubject() {
        Subject subject = new Subject();

        return subject;
    }

    @Test
    public void shouldCreateResource() {
        Subject subject = subjectService.createResource(this.subject);
        assertNotNull(subject.getId());
    }
}
