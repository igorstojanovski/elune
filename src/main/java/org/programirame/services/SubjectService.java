package org.programirame.services;

import org.programirame.models.Subject;
import org.programirame.models.WholeDayEvent;
import org.programirame.repositories.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubjectService {

    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    private final SubjectRepository subjectRepository;

    @Autowired
    DateTimeService dateTimeService;

    @Autowired
    public SubjectService(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    public Subject createResource(Subject subject) {

        List<WholeDayEvent> wholeDayEvents = subject.getWholeDayEvents();

        setWholeDayEventLocalDates(wholeDayEvents);

        Subject createdSubject = subjectRepository.save(subject);

        wholeDayEvents = createdSubject.getWholeDayEvents();

        setWholeDayEventDates(wholeDayEvents);

        return createdSubject;
    }

    private void setWholeDayEventLocalDates(List<WholeDayEvent> wholeDayEvents) {
        for (WholeDayEvent wholeDayEvent : wholeDayEvents) {
            if (wholeDayEvent.getLocalDate() == null) {
                wholeDayEvent.setLocalDate(dateTimeService.getLocalDate(wholeDayEvent.getDate(), YYYY_MM_DD));
            }
        }
    }

    private void setWholeDayEventDates(List<WholeDayEvent> wholeDayEvents) {
        for (WholeDayEvent wholeDayEvent : wholeDayEvents) {
            wholeDayEvent.setDate(dateTimeService.formatLocalDate(wholeDayEvent.getLocalDate(), YYYY_MM_DD));
        }
    }

    public Subject getSubject(long id) {
        Subject subject = subjectRepository.findOne(id);

        List<WholeDayEvent> wholeDayEvents = subject.getWholeDayEvents();

        setWholeDayEventDates(wholeDayEvents);

        return subject;

    }
}
