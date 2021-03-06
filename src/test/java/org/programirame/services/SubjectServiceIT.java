package org.programirame.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.programirame.models.Subject;
import org.programirame.models.Timetable;
import org.programirame.models.utility.HourInterval;
import org.programirame.repositories.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.DayOfWeek;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SubjectServiceIT {

    private static final String START_TIME = "0800";
    private static final String END_TIME = "1700";
    @Autowired
    private SubjectRepository subjectRepository;
    private SubjectService subjectService;

    @Before
    public void setUp() {
        subjectService = new SubjectService(subjectRepository);
    }

    @Test
    public void shouldCreateResource() {

        Subject subject = new Subject();

        Timetable timetable = new Timetable();

        timetable.getDailyHours().put(DayOfWeek.MONDAY, new HourInterval(START_TIME, END_TIME));
        timetable.getDailyHours().put(DayOfWeek.TUESDAY, new HourInterval(START_TIME, END_TIME));
        timetable.getDailyHours().put(DayOfWeek.WEDNESDAY, new HourInterval(START_TIME, END_TIME));
        timetable.getDailyHours().put(DayOfWeek.THURSDAY, new HourInterval(START_TIME, END_TIME));
        timetable.getDailyHours().put(DayOfWeek.FRIDAY, new HourInterval(START_TIME, END_TIME));

        subject.setTimetable(timetable);

        Subject createdSubject = subjectService.createResource(subject);
        assertNotNull(createdSubject.getId());
    }

}
